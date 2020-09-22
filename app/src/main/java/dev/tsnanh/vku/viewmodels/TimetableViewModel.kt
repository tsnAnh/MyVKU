/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dagger.hilt.android.qualifiers.ActivityContext
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableLiveDataUseCase
import dev.tsnanh.vku.utils.getDayOfWeekFromString
import dev.tsnanh.vku.viewmodels.FilterType.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import timber.log.Timber

@FlowPreview
@ExperimentalCoroutinesApi
class TimetableViewModel @ViewModelInject constructor(
    @ActivityContext private val context: Context,
    private val retrieveTimetableUseCase: RetrieveUserTimetableLiveDataUseCase,
    private val client: GoogleSignInClient,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val filterChannel = ConflatedBroadcastChannel<FilterType>()

    val filter: LiveData<Int>? = savedStateHandle["filterType"]

    fun refresh() {
        viewModelScope.launch {
            try {
                val email = GoogleSignIn.getLastSignedInAccount(context)?.email ?: ""
                retrieveTimetableUseCase.refresh(email)
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }

    private val _error = MutableLiveData<Throwable?>()
    val error: LiveData<Throwable?>
        get() = _error

    private fun onError(t: Throwable?) {
        _error.value = t
    }

    fun clearError() {
        _error.value = null
    }

    val timetable = filterChannel.asFlow()
        .flowOn(Dispatchers.Main)
        .flatMapLatest { type ->
            if (type == ALL) {
                retrieveTimetableUseCase.invoke().asFlow().map {
                    it.sortedBy { subject ->
                        try {
                            subject.dayOfWeek.getDayOfWeekFromString()
                        } catch (e: IllegalArgumentException) {
                            null
                        }
                    }
                }.map { it to type }.conflate()
            } else
                retrieveTimetableUseCase.filter(type.value)
                    .map { it to type }
                    .flowOn(Dispatchers.Default)
                    .conflate()
        }.asLiveData()

    fun onFilterAll() = filterChannel.offer(ALL)
    fun onFilterToday() = filterChannel.offer(TODAY)
    fun onFilterTomorrow() = filterChannel.offer(TOMORROW)
}

enum class FilterType(val value: Int) {
    ALL(0), TODAY(1), TOMORROW(2)
}
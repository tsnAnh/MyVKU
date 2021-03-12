/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.timetable

import android.content.SharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.repositories.TimetableFilter
import dev.tsnanh.myvku.domain.repositories.TimetableFilter.ALL
import dev.tsnanh.myvku.domain.repositories.TimetableFilter.TODAY
import dev.tsnanh.myvku.domain.repositories.TimetableFilter.TOMORROW
import dev.tsnanh.myvku.domain.usecases.RetrieveUserTimetableUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class TimetableViewModel @Inject constructor(
    private val retrieveUserTimetableUseCase: RetrieveUserTimetableUseCase,
    private val sharedPreferences: SharedPreferences
) : BaseViewModel() {
    private val filterChannel = ConflatedBroadcastChannel<TimetableFilter>()

    private val email get() = sharedPreferences.getString("email", "")!!

    @OptIn(FlowPreview::class)
    val timetable = filterChannel.asFlow()
        .flowOn(Dispatchers.Main)
        .flatMapLatest { type ->
            retrieveUserTimetableUseCase.invoke(email, type)
        }.conflate()

    init {
        // Default
        onFilterAll()
    }

    fun onFilterAll() = filterChannel.offer(ALL)
    fun onFilterToday() = filterChannel.offer(TODAY)
    fun onFilterTomorrow() = filterChannel.offer(TOMORROW)
}


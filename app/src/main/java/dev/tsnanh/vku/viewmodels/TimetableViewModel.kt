/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableLiveDataUseCase
import dev.tsnanh.vku.utils.getDayOfWeekFromString
import dev.tsnanh.vku.viewmodels.FilterType.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*

@FlowPreview
@ExperimentalCoroutinesApi
class TimetableViewModel @ViewModelInject constructor(
    private val retrieveTimetableUseCase: RetrieveUserTimetableLiveDataUseCase,
) : ViewModel() {
    private val filterChannel = ConflatedBroadcastChannel<FilterType>()
//    val timetable: LiveData<List<Subject>>
//        get() = retrieveTimetableUseCase.invoke()

    init {
        filterChannel.offer(ALL)
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
                }
            } else
                retrieveTimetableUseCase.filter(type.value)
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
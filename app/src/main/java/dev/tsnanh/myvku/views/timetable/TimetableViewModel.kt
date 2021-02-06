/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.timetable

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveUserTimetableUseCase

class TimetableViewModel @ViewModelInject constructor(
    private val retrieveUserTimetableUseCase: RetrieveUserTimetableUseCase,
    @Assisted savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    val filter: LiveData<Int>? = savedStateHandle["filterType"]

    fun getTimetable(email: String) = retrieveUserTimetableUseCase.invoke(email)
}

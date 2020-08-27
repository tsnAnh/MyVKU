/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableLiveDataUseCase

class TimetableViewModel @ViewModelInject constructor(
    retrieveTimetableUseCase: RetrieveUserTimetableLiveDataUseCase
) : ViewModel() {
    val timetable =
        retrieveTimetableUseCase.invoke()
}
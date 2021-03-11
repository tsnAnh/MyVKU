/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.timetable

import android.content.SharedPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.domain.usecases.RetrieveUserTimetableUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val retrieveUserTimetableUseCase: RetrieveUserTimetableUseCase,
    private val sharedPreferences: SharedPreferences,
) : BaseViewModel() {
    private var _timetable =
        retrieveUserTimetableUseCase.invoke(sharedPreferences.getString("email", "")!!.also { println(it) })
    val timetable: Flow<State<List<Subject>>>
        get() = _timetable

    @OptIn(ExperimentalCoroutinesApi::class)
    fun refresh() {
        _timetable = _timetable.flatMapLatest {
            retrieveUserTimetableUseCase.invoke(
                sharedPreferences.getString(
                    "email",
                    ""
                )!!
            )
        }
    }
}

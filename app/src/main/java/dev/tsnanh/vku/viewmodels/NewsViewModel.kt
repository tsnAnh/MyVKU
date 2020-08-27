/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.domain.entities.Absence
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.domain.usecases.RetrieveNewsUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveNoticeUseCase
import kotlinx.coroutines.flow.Flow

class NewsViewModel @ViewModelInject constructor(
    retrieveNewsUseCase: RetrieveNewsUseCase,
    private val retrieveNoticeUseCase: RetrieveNoticeUseCase,
    @Assisted savedStateHandle: SavedStateHandle
) : ViewModel() {
    val news = retrieveNewsUseCase.execute()
    val absences: (String) -> Flow<List<Absence>> = { time ->
        retrieveNoticeUseCase.absence(time)
    }
    val makeUpClass: (String) -> Flow<List<MakeUpClass>> = { time ->
        retrieveNoticeUseCase.makeUpClass(time)
    }
}

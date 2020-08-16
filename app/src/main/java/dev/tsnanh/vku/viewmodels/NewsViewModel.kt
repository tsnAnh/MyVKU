/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.tsnanh.vku.domain.entities.Absence
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.domain.usecases.RetrieveNewsUseCase
import dev.tsnanh.vku.domain.usecases.RetrieveNoticeUseCase
import dev.tsnanh.vku.utils.SecretConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class NewsViewModel : ViewModel() {
    private val retrieveNewsUseCase by inject(RetrieveNewsUseCase::class.java)
    private val retrieveNoticeUseCase by inject(RetrieveNoticeUseCase::class.java)

    val news = retrieveNewsUseCase.execute()
    val absences: (String) -> Flow<List<Absence>> = { time ->
        retrieveNoticeUseCase.absence(time)
    }
    val makeUpClass: (String) -> Flow<List<MakeUpClass>> = {
        retrieveNoticeUseCase.makeUpClass(it)
    }

}

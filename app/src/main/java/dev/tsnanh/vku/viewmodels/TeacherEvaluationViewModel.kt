package dev.tsnanh.vku.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.usecases.RetrieveTeachersUseCase
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import timber.log.Timber

class TeacherEvaluationViewModel @ViewModelInject constructor(
    useCase: RetrieveTeachersUseCase
) : ViewModel() {
    val teachers = useCase.getTeachersLiveData().asLiveData()
}
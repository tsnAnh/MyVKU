package dev.tsnanh.myvku.views.teacherevaluation

import androidx.hilt.lifecycle.ViewModelInject
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveTeachersUseCase

class TeacherEvaluationViewModel @ViewModelInject constructor(
    useCase: RetrieveTeachersUseCase,
) : BaseViewModel() {
    val teachers = useCase.getTeachers()
}
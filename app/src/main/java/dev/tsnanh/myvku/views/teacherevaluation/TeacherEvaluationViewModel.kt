package dev.tsnanh.myvku.views.teacherevaluation

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveTeachersUseCase
import javax.inject.Inject

@HiltViewModel
class TeacherEvaluationViewModel @Inject constructor(
    useCase: RetrieveTeachersUseCase,
) : BaseViewModel() {
    val teachers = useCase.getTeachers()
}

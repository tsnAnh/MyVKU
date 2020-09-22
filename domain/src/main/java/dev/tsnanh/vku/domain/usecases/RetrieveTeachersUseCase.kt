package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Teacher
import dev.tsnanh.vku.domain.repositories.TeacherRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveTeachersUseCase {
    fun getTeachersLiveData(): Flow<Resource<List<Teacher>>>
}

class RetrieveTeachersUseCaseImpl @Inject constructor(
    private val teacherRepo: TeacherRepo
) : RetrieveTeachersUseCase {
    override fun getTeachersLiveData() = teacherRepo.getTeachers()
}
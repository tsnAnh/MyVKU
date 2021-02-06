package dev.tsnanh.myvku.domain.usecases

import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.Teacher
import dev.tsnanh.myvku.domain.repositories.TeacherRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface RetrieveTeachersUseCase {
    fun getTeachers(): Flow<State<List<Teacher>>>
}

class RetrieveTeachersUseCaseImpl @Inject constructor(
    private val teacherRepo: TeacherRepo
) : RetrieveTeachersUseCase {
    override fun getTeachers() = teacherRepo.getTeachers()
}
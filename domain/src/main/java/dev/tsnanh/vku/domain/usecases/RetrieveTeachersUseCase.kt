package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Teacher
import dev.tsnanh.vku.domain.repositories.TeacherRepo
import javax.inject.Inject

interface RetrieveTeachersUseCase {
    fun getTeachersLiveData(): LiveData<Resource<List<Teacher>>>
}

class RetrieveTeachersUseCaseImpl @Inject constructor(
    private val teacherRepo: TeacherRepo
) : RetrieveTeachersUseCase {
    override fun getTeachersLiveData() = teacherRepo.getTeachers().asLiveData()
}
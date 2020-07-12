package dev.tsnanh.vku.domain.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Teacher
import dev.tsnanh.vku.domain.repositories.TeacherRepo
import org.koin.java.KoinJavaComponent.inject

interface RetrieveTeachersUseCase {
    fun getTeachersLiveData(url: String): LiveData<Resource<List<Teacher>>>
}

class RetrieveTeachersUseCaseImpl : RetrieveTeachersUseCase {
    private val teacherRepo by inject(TeacherRepo::class.java)
    override fun getTeachersLiveData(url: String) = teacherRepo.getTeachers(url).asLiveData()
}
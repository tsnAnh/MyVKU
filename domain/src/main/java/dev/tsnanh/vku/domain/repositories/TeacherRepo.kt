package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Teacher
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface TeacherRepo {
    fun getTeachers(): Flow<Resource<List<Teacher>>>
}

class TeacherRepoImpl @Inject constructor() : TeacherRepo {
    override fun getTeachers(): Flow<Resource<List<Teacher>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(VKUServiceApi.network.getAllTeachers()))
        } catch (e: Exception) {
            emit(Resource.Error<List<Teacher>>(e))
        }
    }
}
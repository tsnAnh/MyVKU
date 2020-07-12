package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.entities.Teacher
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface TeacherRepo {
    fun getTeachers(url: String): Flow<Resource<List<Teacher>>>
}

class TeacherRepoImpl : TeacherRepo {
    override fun getTeachers(url: String): Flow<Resource<List<Teacher>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(VKUServiceApi.network.getAllTeachers(url)))
        } catch (e: Exception) {
            emit(ErrorHandler.handleError(e))
        }
    }
}
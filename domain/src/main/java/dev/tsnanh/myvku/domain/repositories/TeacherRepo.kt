package dev.tsnanh.myvku.domain.repositories

import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.Teacher
import dev.tsnanh.myvku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface TeacherRepo {
    fun getTeachers(): Flow<State<List<Teacher>>>
}

class TeacherRepoImpl @Inject constructor() : TeacherRepo {
    override fun getTeachers(): Flow<State<List<Teacher>>> = flow {
        emit(State.Loading())
        try {
            emit(State.Success(VKUServiceApi.network.getAllTeachers()))
        } catch (e: Exception) {
            emit(State.Error<List<Teacher>>(e))
        }
    }
}
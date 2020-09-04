package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.LoginBody
import dev.tsnanh.vku.domain.entities.LoginResponse
import dev.tsnanh.vku.domain.network.VKUServiceApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface UserRepo {
    //    suspend fun getUser(userId: String): Resource<User>
    fun login(idToken: String, body: LoginBody): Flow<LoginResponse>
}

class UserRepoImpl @Inject constructor() : UserRepo {
    override fun login(idToken: String, body: LoginBody): Flow<LoginResponse> =
        flow { emit(VKUServiceApi.network.login(idToken, body)) }
}
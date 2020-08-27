package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.LoginBody
import dev.tsnanh.vku.domain.entities.LoginResponse
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.network.VKUServiceApi
import javax.inject.Inject

interface UserRepo {
    //    suspend fun getUser(userId: String): Resource<User>
    suspend fun login(idToken: String, body: LoginBody): Resource<LoginResponse>
}

class UserRepoImpl @Inject constructor() : UserRepo {
    override suspend fun login(idToken: String, body: LoginBody): Resource<LoginResponse> =
        try {
            Resource.Success(VKUServiceApi.network.login(idToken, body))
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
        }
}
package dev.tsnanh.vku.domain.repositories

import dev.tsnanh.vku.domain.entities.HasUserResponse
import dev.tsnanh.vku.domain.entities.TokenRequestContainer
import dev.tsnanh.vku.domain.entities.User
import dev.tsnanh.vku.domain.network.VKUServiceApi

interface UserRepo {
    suspend fun getUser(userId: String): User
    suspend fun hasUser(userId: TokenRequestContainer): HasUserResponse
    suspend fun signUp(idToken: String): User
}

class UserRepoImpl : UserRepo {
    override suspend fun getUser(userId: String) = VKUServiceApi.network.getUserById(userId)
    override suspend fun hasUser(userId: TokenRequestContainer) =
        VKUServiceApi.network.hasUser(userId)

    override suspend fun signUp(idToken: String) = VKUServiceApi.network.signUp(idToken)
}
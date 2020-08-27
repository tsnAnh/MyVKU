package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.LoginBody
import dev.tsnanh.vku.domain.entities.LoginResponse
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.repositories.UserRepo
import javax.inject.Inject

interface LoginUseCase {
    suspend fun execute(idToken: String, body: LoginBody): Resource<LoginResponse>
}

class LoginUseCaseImpl @Inject constructor(
    private val userRepo: UserRepo
) : LoginUseCase {
    override suspend fun execute(idToken: String, body: LoginBody): Resource<LoginResponse> {
        return try {
            userRepo.login(idToken, body)
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
        }
    }
}
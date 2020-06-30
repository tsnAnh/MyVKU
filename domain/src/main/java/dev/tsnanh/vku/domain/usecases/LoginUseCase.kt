package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.LoginResponse
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.repositories.UserRepo
import org.koin.java.KoinJavaComponent.inject

interface LoginUseCase {
    suspend fun execute(idToken: String, body: Any): Resource<LoginResponse>
}

class LoginUseCaseImpl : LoginUseCase {
    private val userRepo by inject(UserRepo::class.java)
    override suspend fun execute(idToken: String, body: Any): Resource<LoginResponse> {
        return try {
            userRepo.login(idToken, body)
        } catch (e: Exception) {
            ErrorHandler.handleError(e)
        }
    }
}
package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.HasUserResponse
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.handler.ErrorHandler
import dev.tsnanh.vku.domain.repositories.UserRepo
import org.koin.java.KoinJavaComponent.inject

interface LoginUseCase {
    suspend fun execute(idToken: String): Resource<HasUserResponse>
}

class LoginUseCaseImpl : LoginUseCase {
    private val userRepo by inject(UserRepo::class.java)
    override suspend fun execute(idToken: String): Resource<HasUserResponse> {
        return try {
            userRepo.login(idToken)
        } catch (e: Throwable) {
            ErrorHandler.handleError(e)
        }
    }
}
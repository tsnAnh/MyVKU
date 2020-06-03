package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.User
import dev.tsnanh.vku.domain.repositories.UserRepo
import org.koin.java.KoinJavaComponent.inject

interface SignUpUseCase {
    suspend fun execute(idToken: String): User
}

class SignUpUseCaseImpl : SignUpUseCase {
    private val userRepo by inject(UserRepo::class.java)
    override suspend fun execute(idToken: String): User {
        return userRepo.signUp(idToken)
    }
}
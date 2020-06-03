package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.HasUserResponse
import dev.tsnanh.vku.domain.entities.TokenRequestContainer
import dev.tsnanh.vku.domain.repositories.UserRepo
import org.koin.java.KoinJavaComponent.inject

interface CheckHasUserUseCase {
    suspend fun execute(userId: TokenRequestContainer): HasUserResponse
}

class CheckHasUserUseCaseImpl : CheckHasUserUseCase {
    private val userRepo by inject(UserRepo::class.java)
    override suspend fun execute(userId: TokenRequestContainer): HasUserResponse {
        return userRepo.hasUser(userId)
    }
}
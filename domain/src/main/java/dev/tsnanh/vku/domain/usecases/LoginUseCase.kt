package dev.tsnanh.vku.domain.usecases

import dev.tsnanh.vku.domain.entities.LoginBody
import dev.tsnanh.vku.domain.entities.LoginResponse
import dev.tsnanh.vku.domain.repositories.UserRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface LoginUseCase {
    fun execute(idToken: String, body: LoginBody): Flow<LoginResponse>
}

class LoginUseCaseImpl @Inject constructor(
    private val userRepo: UserRepo
) : LoginUseCase {
    override fun execute(idToken: String, body: LoginBody) = userRepo.login(idToken, body)
}
package dev.tsnanh.vku.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.domain.entities.LoginBody
import dev.tsnanh.vku.domain.usecases.LoginUseCase
import kotlinx.coroutines.coroutineScope

class WelcomeViewModel @ViewModelInject constructor(
    private val checkHasUserUseCase: LoginUseCase
) : ViewModel() {
    suspend fun login(idToken: String, body: LoginBody) = coroutineScope {
        checkHasUserUseCase.execute(idToken, body)
    }
}
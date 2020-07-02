package dev.tsnanh.vku.viewmodels.my_vku

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.tsnanh.vku.domain.entities.LoginBody
import dev.tsnanh.vku.domain.usecases.LoginUseCase
import kotlinx.coroutines.coroutineScope
import org.koin.java.KoinJavaComponent.inject

class WelcomeViewModel(application: Application) : AndroidViewModel(application) {
    private val checkHasUserUseCase by inject(LoginUseCase::class.java)

    suspend fun login(idToken: String, body: LoginBody) = coroutineScope {
        checkHasUserUseCase.execute(idToken, body)
    }
}
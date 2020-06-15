package dev.tsnanh.vku.viewmodels.my_vku

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dev.tsnanh.vku.domain.usecases.CheckHasUserUseCase
import kotlinx.coroutines.coroutineScope
import org.koin.java.KoinJavaComponent.inject

class WelcomeViewModel(application: Application) : AndroidViewModel(application) {
    private val checkHasUserUseCase by inject(CheckHasUserUseCase::class.java)

    suspend fun hasUser(idToken: String) = coroutineScope {
        checkHasUserUseCase.execute(idToken)
    }
}
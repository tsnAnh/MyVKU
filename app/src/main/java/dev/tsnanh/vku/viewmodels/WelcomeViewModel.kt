package dev.tsnanh.vku.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import dev.tsnanh.vku.domain.entities.TokenRequestContainer
import dev.tsnanh.vku.domain.usecases.CheckHasUserUseCase
import dev.tsnanh.vku.utils.FirebaseUserLiveData
import kotlinx.coroutines.coroutineScope
import org.koin.java.KoinJavaComponent.inject

class WelcomeViewModel(application: Application) : AndroidViewModel(application) {
    private val checkHasUserUseCase by inject(CheckHasUserUseCase::class.java)

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthenticationState.AUTHENTICATED
        } else {
            AuthenticationState.UNAUTHENTICATED
        }
    }

    private val _signIn = MutableLiveData<Boolean>()
    val signIn: LiveData<Boolean>
        get() = _signIn

    fun onSignInClick() {
        _signIn.value = true
    }

    fun onSignInClicked() {
        _signIn.value = null
    }

    suspend fun hasUser(uid: String) = coroutineScope {
        checkHasUserUseCase.execute(TokenRequestContainer(uid))
    }

    enum class AuthenticationState {
        AUTHENTICATED, UNAUTHENTICATED
    }
}
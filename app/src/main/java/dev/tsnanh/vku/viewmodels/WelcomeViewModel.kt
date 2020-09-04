package dev.tsnanh.vku.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.iid.FirebaseInstanceId
import dev.tsnanh.vku.domain.entities.LoginBody
import dev.tsnanh.vku.domain.entities.LoginResponse
import dev.tsnanh.vku.domain.usecases.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred

@ExperimentalCoroutinesApi
class WelcomeViewModel @ViewModelInject constructor(
    private val checkHasUserUseCase: LoginUseCase,
    private val client: GoogleSignInClient,
) : ViewModel() {
    private var _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse>
        get() = _loginResponse

    private var _loginState = MutableLiveData(LoginState.UNAUTHENTICATED)
    val loginState: LiveData<LoginState>
        get() = _loginState

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable>
        get() = _error

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            val deferredIdToken = client.silentSignIn().asDeferred()
            val deferredInstanceId = FirebaseInstanceId.getInstance().instanceId.asDeferred()

            val idToken = deferredIdToken.await().idToken
            val tokenFCM = deferredInstanceId.await().token

            if (idToken != null) {
                checkHasUserUseCase.execute(idToken = idToken, LoginBody(tokenFCM = tokenFCM))
                    .flowOn(Dispatchers.IO)
                    .onStart { _loginState.postValue(LoginState.AUTHENTICATING) }
                    .catch { t -> _error.postValue(t) }
                    .collect { res ->
                        _loginResponse.postValue(res)
                    }
            }
        }
    }
}

enum class LoginState {
    AUTHENTICATING, UNAUTHENTICATED, AUTHENTICATED
}
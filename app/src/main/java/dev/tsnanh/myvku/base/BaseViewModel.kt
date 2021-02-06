package dev.tsnanh.myvku.base

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow

abstract class BaseViewModel : ViewModel() {
    private val _user = MutableStateFlow<GoogleSignInAccount?>(null)
    val user: StateFlow<GoogleSignInAccount?>
        get() = _user

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getNetworkAvailability(connectivityManager: ConnectivityManager?) = callbackFlow<Boolean> {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                offer(true)
            }

            override fun onLost(network: Network) {
                offer(false)
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            connectivityManager?.registerDefaultNetworkCallback(networkCallback)
        } else {
            connectivityManager?.registerNetworkCallback(NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build(), networkCallback)
        }
    }

    fun setUser(account: GoogleSignInAccount?) {
        _user.value = account
    }

    private val _error = MutableStateFlow<Throwable?>(null)
    val error: StateFlow<Throwable?>
        get() = _error

    fun onError(t: Throwable?) {
        _error.value = t
    }

    fun clearError() {
        _error.value = null
    }
}

package dev.tsnanh.vku.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

class ConnectivityLiveData(
    private val connectivityManager: ConnectivityManager?,
) : LiveData<Boolean>() {
    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }
    }

    private val networkRequest = NetworkRequest.Builder()

    override fun onActive() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager?.registerDefaultNetworkCallback(callback)
        } else {
            connectivityManager?.registerNetworkCallback(networkRequest.build(), callback)
        }
    }

    override fun onInactive() {
        connectivityManager?.unregisterNetworkCallback(callback)
    }
}
package dev.tsnanh.vku.utils

import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData

@RequiresApi(Build.VERSION_CODES.N)
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

    override fun onActive() {
        connectivityManager?.registerDefaultNetworkCallback(callback)
    }

    override fun onInactive() {
        connectivityManager?.unregisterNetworkCallback(callback)
    }
}
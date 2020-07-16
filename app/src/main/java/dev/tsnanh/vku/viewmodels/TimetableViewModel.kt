/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableLiveDataUseCase
import dev.tsnanh.vku.utils.SecretConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.net.SocketException
import java.net.SocketTimeoutException

class TimetableViewModel(application: Application) : AndroidViewModel(application) {
    private val retrieveTimetableUseCase by inject(RetrieveUserTimetableLiveDataUseCase::class.java)
    val timetable =
        retrieveTimetableUseCase.invoke()

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun onErrorDisplayed() {
        _error.value = null
    }

    fun refreshSubjects(email: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    retrieveTimetableUseCase.refresh(SecretConstants.TKB_URL, email)
                } catch (ste: SocketTimeoutException) {
                    _error.postValue(getApplication<Application>().getString(R.string.err_msg_request_timeout))
                } catch (ske: SocketException) {
                    _error.postValue(getApplication<Application>().getString(R.string.err_msg_socket_exception))
                } catch (e: Exception) {
                    _error.postValue(getApplication<Application>().getString(R.string.err_msg_something_went_wrong))
                }
            }
        }
    }
}

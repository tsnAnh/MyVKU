/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dev.tsnanh.vku.domain.usecases.RetrieveUserTimetableLiveDataUseCase
import dev.tsnanh.vku.utils.isInternetAvailable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import timber.log.Timber

class TimetableViewModel(application: Application) : AndroidViewModel(application) {
    private val retrieveTimetableUseCase by inject(RetrieveUserTimetableLiveDataUseCase::class.java)
    val timetable =
        retrieveTimetableUseCase.invoke()


}

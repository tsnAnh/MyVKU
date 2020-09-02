/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dev.tsnanh.vku.domain.entities.Notification
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.usecases.RetrieveNotificationsUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.asDeferred
import java.lang.IllegalStateException

class NotificationsViewModel @ViewModelInject constructor(
    private val retrieveNotificationsUseCase: RetrieveNotificationsUseCase,
    private val client: GoogleSignInClient,
) : ViewModel() {
    suspend fun getNotifications(): LiveData<Resource<List<Notification>>> {
        val deferred = client.silentSignIn().asDeferred()

        val idToken = deferred.await().idToken!!
        return retrieveNotificationsUseCase.getNotifications(idToken)
    }
}
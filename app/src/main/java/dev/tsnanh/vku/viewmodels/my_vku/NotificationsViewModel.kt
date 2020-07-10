/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels.my_vku

import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.domain.usecases.RetrieveNotificationsUseCase
import org.koin.java.KoinJavaComponent.inject

class NotificationsViewModel : ViewModel() {
    private val retrieveNotificationsUseCase by inject(RetrieveNotificationsUseCase::class.java)

    fun getNotifications(idToken: String) = retrieveNotificationsUseCase.getNotifications(idToken)
}

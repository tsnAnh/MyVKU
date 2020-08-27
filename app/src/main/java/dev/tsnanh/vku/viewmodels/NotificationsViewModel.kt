/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.domain.usecases.RetrieveNotificationsUseCase

class NotificationsViewModel @ViewModelInject constructor(
    private val retrieveNotificationsUseCase: RetrieveNotificationsUseCase
) : ViewModel() {
    fun getNotifications(idToken: String) = retrieveNotificationsUseCase.getNotifications(idToken)
}

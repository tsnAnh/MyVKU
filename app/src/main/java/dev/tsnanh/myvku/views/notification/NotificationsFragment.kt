/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.databinding.FragmentNotificationsBinding
import dev.tsnanh.myvku.views.notification.adapter.NotificationAdapter
import dev.tsnanh.myvku.views.notification.adapter.NotificationClickListener

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<NotificationsViewModel, FragmentNotificationsBinding>() {
    override val viewModel: NotificationsViewModel by viewModels()
    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = FragmentNotificationsBinding.inflate(inflater, container, false)

    override fun FragmentNotificationsBinding.initViews() {
        val notificationAdapter = NotificationAdapter(
            NotificationClickListener {
                // viewModel.onNotificationClick(it.)
            }
        )
        listNotifications.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }
    }

    override suspend fun NotificationsViewModel.observeData() {

    }
}

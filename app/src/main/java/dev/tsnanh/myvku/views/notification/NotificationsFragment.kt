/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.databinding.FragmentNotificationsBinding
import dev.tsnanh.myvku.views.notification.adapter.NotificationAdapter
import dev.tsnanh.myvku.views.notification.adapter.NotificationClickListener

@AndroidEntryPoint
class NotificationsFragment : BaseFragment() {
    override val viewModel: NotificationsViewModel by viewModels()
    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner


    }

    override fun setupView() {
        val notificationAdapter = NotificationAdapter(NotificationClickListener {
            // viewModel.onNotificationClick(it.)
        })
        binding.listNotifications.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }
    }

    override fun bindView() {
    }
}

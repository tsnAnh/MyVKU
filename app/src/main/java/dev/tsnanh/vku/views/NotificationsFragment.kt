/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.NotificationAdapter
import dev.tsnanh.vku.adapters.NotificationClickListener
import dev.tsnanh.vku.databinding.FragmentNotificationsBinding
import dev.tsnanh.vku.domain.entities.Notification
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.viewmodels.NotificationsViewModel

@AndroidEntryPoint
class NotificationsFragment : Fragment() {
    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        val notificationAdapter = NotificationAdapter(NotificationClickListener {
            // viewModel.onNotificationClick(it.)
        })
        binding.listNotifications.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }

        lifecycleScope.launchWhenCreated {
            viewModel.getNotifications()
                ?.observe<Resource<List<Notification>>>(viewLifecycleOwner) { resource ->
                    fun toggleUI(progressBarVisibility: Boolean, errorLayoutVisibility: Boolean) {
                        binding.progressBar.isVisible = progressBarVisibility
                        binding.include.errorLayout.isVisible = errorLayoutVisibility
                    }
                    when (resource) {
                        is Resource.Loading -> toggleUI(
                            progressBarVisibility = true,
                            errorLayoutVisibility = false
                        )
                        is Resource.Error -> toggleUI(
                            progressBarVisibility = false,
                            errorLayoutVisibility = true
                        )
                        is Resource.Success -> {
                            toggleUI(
                                progressBarVisibility = false,
                                errorLayoutVisibility = false
                            )
                            resource.data?.let { notifications ->
                                if (notifications.isEmpty()) binding.layoutNoNotifications.isVisible =
                                    true
                                notificationAdapter.submitList(resource.data)
                            }
                        }
                    }
                }
        }
    }
}

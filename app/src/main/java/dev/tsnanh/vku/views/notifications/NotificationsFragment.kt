/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.NotificationAdapter
import dev.tsnanh.vku.adapters.NotificationClickListener
import dev.tsnanh.vku.databinding.FragmentNotificationsBinding
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.isInternetAvailable
import dev.tsnanh.vku.utils.showSnackbarWithAction
import dev.tsnanh.vku.viewmodels.NotificationsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsFragment : Fragment() {

    private val viewModel: NotificationsViewModel by viewModels()
    private lateinit var binding: FragmentNotificationsBinding
    @Inject lateinit var client: GoogleSignInClient

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

        client.silentSignIn().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                viewModel.getNotifications(result.result?.idToken!!)
                    .observe(viewLifecycleOwner, {
                        it?.let {
                            when (it) {
                                is Resource.Success -> {
                                    binding.progressBar.isVisible = false
                                    binding.include.errorLayout.isVisible = false
                                    notificationAdapter.submitList(it.data)
                                }
                                is Resource.Loading -> {
                                    binding.progressBar.isVisible = true
                                    binding.include.errorLayout.isVisible = false
                                }
                                is Resource.Error -> {
                                    binding.progressBar.isVisible = false
                                    if (isInternetAvailable(requireContext())) {
                                        showSnackbarWithAction(
                                            requireView(),
                                            requireContext().getString(
                                                R.string.err_msg_something_went_wrong
                                            )
                                        )
                                    } else {
                                        binding.include.apply {
                                            errorLayout.isVisible = true
                                            textView7.text = "No Internet Connection"
                                        }
                                    }
                                }
                            }
                        }
                    })
            }
        }
    }
}

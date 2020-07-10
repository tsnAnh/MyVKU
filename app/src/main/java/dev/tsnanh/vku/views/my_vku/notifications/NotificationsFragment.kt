/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.my_vku.notifications

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
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.NotificationAdapter
import dev.tsnanh.vku.databinding.FragmentNotificationsBinding
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.showSnackbarWithAction
import dev.tsnanh.vku.viewmodels.my_vku.NotificationsViewModel
import org.koin.java.KoinJavaComponent.inject

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        val notificationAdapter = NotificationAdapter()
        binding.listNotifications.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = notificationAdapter
        }

        val mGoogleSignInClient by inject(GoogleSignInClient::class.java)
        mGoogleSignInClient.silentSignIn().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                viewModel.getNotifications(result.result?.idToken!!)
                    .observe(viewLifecycleOwner, Observer {
                        it?.let {
                            when (it) {
                                is Resource.Success -> {
                                    binding.progressBar.isVisible = false
                                    notificationAdapter.submitList(it.data)
                                }
                                is Resource.Loading -> {
                                    binding.progressBar.isVisible = true
                                }
                                is Resource.Error -> {
                                    binding.progressBar.isVisible = false
                                    showSnackbarWithAction(
                                        requireView(),
                                        requireContext().getString(R.string.err_msg_something_went_wrong)
                                    )
                                }
                            }
                        }
                    })
            }
        }
    }
}

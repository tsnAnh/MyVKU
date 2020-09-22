/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ForumAdapter
import dev.tsnanh.vku.adapters.ForumClickListener
import dev.tsnanh.vku.databinding.FragmentForumBinding
import dev.tsnanh.vku.domain.entities.NetworkForum
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.showSnackbar
import dev.tsnanh.vku.viewmodels.ForumViewModel
import dev.tsnanh.vku.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
@Deprecated(message = "unused feature")
@AndroidEntryPoint
class ForumFragment : Fragment() {

    private lateinit var binding: FragmentForumBinding
    private val viewModel: ForumViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = Hold()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_forum, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.listTopics.apply {
            setHasFixedSize(true)
            val rotation = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                @Suppress("DEPRECATION")
                (requireContext()
                    .getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                    .defaultDisplay
                    .rotation
            } else {
                requireContext().display?.rotation
            }
            layoutManager = if (rotation == 0) {
                LinearLayoutManager(requireContext())
            } else {
                GridLayoutManager(requireContext(), 2)
            }
            itemAnimator = null
        }

        val adapter = ForumAdapter(ForumClickListener { forum, cardView ->
            viewModel.onItemClick(forum to cardView)
        })

        binding.listTopics.adapter = adapter

        viewModel.forums.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    with(binding) {
                        progressBar.isVisible = true
                        layoutNoItem.root.isVisible = false
                        listTopics.isVisible = false
                    }
                }
                is Resource.Error -> {
                    viewModel.onError(resource.throwable)
                }
                is Resource.Success -> {
                    with(binding) {
                        listTopics.isVisible = true
                        progressBar.isVisible = false
                        layoutNoItem.root.isVisible = false
                        swipeToRefresh.isRefreshing = false
                        val forums = resource.data
                        if (forums != null && forums.isNotEmpty()) {
                            adapter.submitList(resource.data)
                        } else {
                            showLayout(requireContext().getString(R.string.text_application_has_no_forums),
                                R.drawable.ic_round_forum_24)
                        }
                    }
                }
            }
        }

        viewModel.navigateToListThread.observe<Pair<NetworkForum, MaterialCardView>?>(
            viewLifecycleOwner
        ) {
            it?.let { (first, second) ->
                val extras = FragmentNavigatorExtras(
                    second to first.id
                )
                findNavController().navigate(
                    ForumFragmentDirections.actionNavigationForumToNavigationThread(
                        first.id,
                        first.title
                    ),
                    extras
                )
                viewModel.onItemClicked()
            }
        }

        binding.fabNewThread.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.fabNewThread to Constants.FAB_TRANSFORM_TO_NEW_THREAD
            )
            findNavController().navigate(
                ForumFragmentDirections.actionNavigationForumToNavigationNewThread(
                    null,
                    null
                ),
                extras
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.listTopics.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY <= oldScrollY) {
                    binding.fabNewThread.show()
                } else {
                    binding.fabNewThread.hide()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { t ->
            t?.let {
                when (t) {
                    is ConnectException -> {
                        binding.progressBar.isVisible = false
                        showLayout(requireContext().getString(R.string.text_no_internet_connection),
                            R.drawable.ic_baseline_wifi_off_24)
                        showSnackbar(requireView(), requireContext().getString(R.string.text_no_internet_connection))
                    }
                    is SocketTimeoutException -> {
                        showSnackbar(requireView(), requireContext().getString(R.string.err_msg_request_timeout))
                        showLayout(requireContext().getString(R.string.err_msg_request_timeout), R.drawable.sad)
                    }
                    is UnknownHostException -> {
                        binding.progressBar.isVisible = false
                        showLayout("Unknown host",
                            R.drawable.sad)
                        showSnackbar(view, "Unknown host")
                    }
                    is SocketException -> {
                        showSnackbar(view, "Disconnected")
                    }
                    else -> {
                        Timber.e(t)
                        binding.swipeToRefresh.isRefreshing = false
                        showLayout(requireContext().getString(R.string.text_something_went_wrong), R.drawable.sad)
                    }
                }
                with(binding) {
                    progressBar.isVisible = false
                    swipeToRefresh.isVisible = false
                }

                binding.swipeToRefresh.isVisible = false
                viewModel.clearError()
            }
        }

        activityViewModel.connectivityLiveData.observe(viewLifecycleOwner) { available ->
            if (available) {
                viewModel.refresh()
            }/* else {
//                showLayout(requireContext().getString(R.string.text_no_internet_connection),
//                    R.drawable.ic_baseline_wifi_off_24)
            } */
        }

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    private fun showLayout(messageString: String, drawable: Int) {
        with(binding.layoutNoItem) {
            message.text = messageString
            image.setImageResource(drawable)
            root.isVisible = true
        }
    }
}


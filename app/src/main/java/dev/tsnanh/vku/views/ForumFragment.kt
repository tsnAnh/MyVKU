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
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
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
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.viewmodels.ForumViewModel
import dev.tsnanh.vku.viewmodels.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

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

        lifecycleScope.launch {
            viewModel.forums
                .onStart { binding.progressBar.isVisible = true }
                .catch { _ -> binding.errorLayout.root.isVisible = true }
                .asLiveData()
                .observe<List<NetworkForum>>(viewLifecycleOwner) { forums ->
                    binding.progressBar.isVisible = false
                    binding.errorLayout.root.isVisible = false
                    binding.noForumsLayout.isVisible = forums.isEmpty()
                    adapter.submitList(forums)
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
    }
}


/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ListRepliesPagerAdapter
import dev.tsnanh.vku.databinding.FragmentReplyBinding
import dev.tsnanh.vku.domain.entities.ReplyContainer
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.showSnackbarWithAction
import dev.tsnanh.vku.viewmodels.RepliesViewModel

@AndroidEntryPoint
class ReplyFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private lateinit var binding: FragmentReplyBinding
    private val viewModel: RepliesViewModel by viewModels()
    private val navArgs: ReplyFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()
        exitTransition = Hold()

        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_reply, container, false)

        binding.bottomAppBar.apply {
            setOnMenuItemClickListener(this@ReplyFragment)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.layout.transitionName = navArgs.threadId

        binding.fabReply.transitionName = Constants.FAB_TRANSFORM_TO_NEW_REPLY

        with(binding.viewPager) {
            offscreenPageLimit = 1
            animation = null
            // Maybe more
        }

        with(viewModel) {
            pageCount(navArgs.threadId).observe<Resource<ReplyContainer>>(viewLifecycleOwner) { result ->
                when (result) {
                    is Resource.Success -> {
                        val adapter = ListRepliesPagerAdapter(
                            this@ReplyFragment,
                            navArgs.threadId,
                            result.data!!.totalPages
                        )
                        binding.viewPager.adapter = adapter

                        binding.viewPager.currentItem = if (navArgs.hasCreatedReply) {
                            adapter.itemCount
                        } else {
                            0
                        }
                    }
                }
            }
        }

        binding.fabReply.setOnClickListener {
            openNewReplyDialog()
        }
    }

    private fun openNewReplyDialog(quotedPostId: String? = null) {
        val extras = FragmentNavigatorExtras(
            binding.fabReply to Constants.FAB_TRANSFORM_TO_NEW_REPLY
        )
        findNavController().navigate(
            ReplyFragmentDirections.actionNavigationRepliesToNewReplyFragment(
                navArgs.threadId, quotedPostId
            ),
            extras
        )
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_first_page -> {
                binding.viewPager.currentItem = 0
                true
            }
            R.id.action_last_page -> {
                with(binding.viewPager) { adapter?.itemCount?.let { currentItem = it } }
                true
            }
            R.id.action_next_page -> {
                with(binding.viewPager) { currentItem++ }
                true
            }
            R.id.action_previous_page -> {
                with(binding.viewPager) { currentItem-- }
                true
            }
            else -> {
                showSnackbarWithAction(requireView(),
                    requireContext().getString(R.string.text_something_went_wrong))
                true
            }
        }
    }

    companion object {
        const val EDIT_ITEM_ORDER = 0
        const val DELETE_ITEM_ORDER = 1
        const val REPORT_ITEM_ORDER = 2
    }
}
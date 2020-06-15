/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.my_vku.replies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.WorkManager
import com.google.android.material.transition.MaterialContainerTransform
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ListRepliesPagerAdapter
import dev.tsnanh.vku.databinding.FragmentRepliesBinding
import dev.tsnanh.vku.utils.Constants.Companion.BOTTOM_SHEET_TAG
import dev.tsnanh.vku.viewmodels.my_vku.RepliesViewModel
import dev.tsnanh.vku.viewmodels.my_vku.RepliesViewModelFactory
import dev.tsnanh.vku.views.my_vku.replies.createnewreply.NewReplyFragment
import timber.log.Timber

class RepliesFragment : Fragment() {

    private lateinit var binding: FragmentRepliesBinding
    private lateinit var viewModel: RepliesViewModel
    private val navArgs: RepliesFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_replies, container, false)

        setHasOptionsMenu(true)
        binding.bottomAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Timber.d(navArgs.threadId)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.layout.transitionName = navArgs.threadId
        viewModel = ViewModelProvider(
            this,
            RepliesViewModelFactory(
                navArgs.threadId,
                requireActivity().application
            )
        ).get(RepliesViewModel::class.java)

        binding.viewPager.apply { setCurrentItem(0, false) }

        viewModel.replies.observe(viewLifecycleOwner, Observer {
            it?.let {
                val adapter = ListRepliesPagerAdapter(this, navArgs.threadId, it.first.totalPages)
                binding.viewPager.adapter = adapter
                if (it.second) {
                    binding.viewPager.currentItem = it.first.totalPages
                } else {
                    binding.viewPager.currentItem = 0
                }
            }
        })

        viewModel.createPostWorkerLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) return@Observer
            WorkManager.getInstance(requireContext()).pruneWork()
            viewModel.refresh(true)
        })

        binding.fabReply.setOnClickListener {
            openNewReplyDialog()
        }
    }

    fun openNewReplyDialog(quotedPostId: String = "") {
        val sheet =
            NewReplyFragment(
                navArgs.threadId,
                navArgs.threadTitle,
                quotedPostId
            )
        sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle)
        sheet.show(
            childFragmentManager,
            BOTTOM_SHEET_TAG
        )
    }
}
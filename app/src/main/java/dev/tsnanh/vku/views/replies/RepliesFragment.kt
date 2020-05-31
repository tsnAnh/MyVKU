/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.replies

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
import dev.tsnanh.vku.viewmodels.RepliesViewModel
import dev.tsnanh.vku.viewmodels.RepliesViewModelFactory
import dev.tsnanh.vku.views.replies.createnewreply.NewReplyFragment
import timber.log.Timber

const val POST_TAG = "create_post"

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

        binding.viewPager.apply {
            setCurrentItem(0, false)
        }

        viewModel.replies.observe(viewLifecycleOwner, Observer {
            it?.let {
                val adapter = ListRepliesPagerAdapter(this, navArgs.threadId, it.totalPages)
                binding.viewPager.adapter = adapter
            }
        })

        viewModel.createPostWorkerLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                return@Observer
            }
            WorkManager.getInstance(requireContext()).pruneWork()
            viewModel.refresh()
        })

        binding.fabReply.setOnClickListener {
            openNewReplyDialog()
        }
    }

    fun openNewReplyDialog(quotedPostId: String = "") {
        val sheet = NewReplyFragment(navArgs.threadId, navArgs.threadTitle, quotedPostId)
        sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle)
        sheet.show(childFragmentManager, TAG_BOTTOM_SHEET)
    }
}

private const val TAG_BOTTOM_SHEET = "new_reply"
/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.view.replies

import android.app.NotificationManager
import android.content.Context
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkManager
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.RepliesAdapter
import dev.tsnanh.vku.databinding.FragmentRepliesBinding
import dev.tsnanh.vku.network.NetworkPost
import dev.tsnanh.vku.utils.sendNotification
import dev.tsnanh.vku.view.replies.newreply.NewReplyFragment
import timber.log.Timber

class RepliesFragment : Fragment() {

    private lateinit var binding: FragmentRepliesBinding
    private lateinit var viewModel: RepliesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        binding.bottomAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navArgs: RepliesFragmentArgs by navArgs()
        Timber.d(navArgs.threadId)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(
            this,
            RepliesViewModelFactory(navArgs.threadId, requireActivity().application)
        ).get(RepliesViewModel::class.java)

        binding.listReplies.setHasFixedSize(true)
        binding.listReplies.layoutManager = LinearLayoutManager(requireContext())

        val adapter = RepliesAdapter()
        binding.listReplies.adapter = adapter

        viewModel.replies.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.createPostWorkerLiveData.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                return@Observer
            }
            val workInfo = it[0]

            if (workInfo.state.isFinished) {
                val jsonAdapter =
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        .adapter(NetworkPost::class.java)
                val json = workInfo.outputData.getString("thread")
                if (json != null && json.isNotEmpty()) {
                    val post = jsonAdapter.fromJson(json)
                    post?.let {
                        (requireContext()
                            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                            .sendNotification(
                                post.content,
                                "${post.content} is successfully created!",
                                requireContext()
                            )
                    }
                }
                WorkManager.getInstance(requireContext()).pruneWork()
                viewModel.refresh()
            }
        })

        binding.fabReply.setOnClickListener {
            val sheet = NewReplyFragment(navArgs.threadId, navArgs.threadTitle)
            sheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogStyle)
            sheet.show(childFragmentManager, TAG_BOTTOM_SHEET)
        }
    }

}

private const val TAG_BOTTOM_SHEET = "new_reply"
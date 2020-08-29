/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ThreadAdapter
import dev.tsnanh.vku.adapters.ThreadClickListener
import dev.tsnanh.vku.databinding.FragmentThreadBinding
import dev.tsnanh.vku.databinding.ProgressDialogLayoutBinding
import dev.tsnanh.vku.domain.entities.NetworkForumThreadCustom
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.viewmodels.ThreadViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class ThreadFragment : Fragment() {
    private val viewModel: ThreadViewModel by viewModels()
    private lateinit var binding: FragmentThreadBinding
    private lateinit var adapter: ThreadAdapter
    private lateinit var progressBarLayoutBinding: ProgressDialogLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()
        exitTransition = Hold()

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_thread, container, false)

        progressBarLayoutBinding =
            ProgressDialogLayoutBinding.inflate(LayoutInflater.from(requireContext()))

        val args: ThreadFragmentArgs by navArgs()
        binding.toolbar.title = args.title
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navArgs: ThreadFragmentArgs by navArgs()
        binding.layoutThread.transitionName = navArgs.id

        configureList()
        adapter = ThreadAdapter(
            GoogleSignIn.getLastSignedInAccount(requireContext())?.id!!,
            ThreadClickListener { thread, cardView ->
                viewModel.onNavigateToReplies(thread, cardView)
            })

        binding.listThread.adapter = adapter

        viewModel.getThreads(navArgs.id).observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        adapter.submitList(it.data!!.sortedByDescending { forum ->
                            forum.createdAt
                        })
                    }
                    is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is Resource.Error -> {
                        Snackbar
                            .make(requireView(), "${it.message}", Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.navigateToReplies.observe(viewLifecycleOwner) {
            it?.let {
                val extras = FragmentNavigatorExtras(
                    it.second to it.first.id
                )
                findNavController().navigate(
                    ThreadFragmentDirections.actionNavigationThreadToNavigationReplies(
                        it.first.id
                    ),
                    extras
                )
                viewModel.onNavigatedToReplies()
            }
        }

        binding.fabNew.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                binding.fabNew to Constants.FAB_TRANSFORM_TO_NEW_THREAD
            )
            findNavController().navigate(
                ThreadFragmentDirections.actionNavigationThreadToNavigationNewThread(
                    navArgs.id,
                    navArgs.title
                ),
                extras
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.listThread.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY <= oldScrollY) {
                    binding.fabNew.show()
                } else {
                    binding.fabNew.hide()
                }
            }
        }
    }

    private fun configureList() {
        binding.listThread.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = null
        }
    }
//
//    override fun onContextItemSelected(item: MenuItem): Boolean {
//        val currentItemId = adapter.currentList[item.itemId].id
//        val titleThread = adapter.currentList[item.itemId].title
//        when (item.order) {
//            EDIT_ITEM_ORDER -> {
//                val binding = LayoutEditThreadTitleDialogBinding
//                    .inflate(LayoutInflater.from(requireContext())).apply {
//                        inputEditText.setText(titleThread)
//                    }
//                val builder = MaterialAlertDialogBuilder(requireContext())
//                    .setTitle("Edit thread title")
//                    .setView(binding.root)
//                    .setPositiveButton("Confirm") { d, _ ->
//                        client.silentSignIn().addOnSuccessListener { result ->
//                            viewModel.updateThreadTitle(
//                                result.idToken!!,
//                                currentItemId,
//                                UpdateThreadBody(binding.inputEditText.text.toString())
//                            ).observe(viewLifecycleOwner) { resource ->
//                                when (resource) {
//                                    is Resource.Loading -> {
//                                        progressDialog.show()
//                                    }
//                                    is Resource.Error -> showSnackbarWithAction(
//                                        requireView(),
//                                        "An error has occurred. Please try again later!"
//                                    ).also {
//                                        progressDialog.hide()
//                                    }
//                                    is Resource.Success -> {
//                                        viewModel.refreshThreadsLiveData()
//                                            .observe(viewLifecycleOwner) {
//                                                refresh(it)
//                                            }
//                                        progressDialog.hide()
//                                    }
//                                }
//                            }
//                            d.dismiss()
//                        }
//                    }
//                    .setNegativeButton("Cancel") { d, _ ->
//                        d.dismiss()
//                    }
//                    .create().show()
//            }
//            DELETE_ITEM_ORDER -> {
//                client.silentSignIn().addOnSuccessListener {
//                    viewModel.deleteThread(it.idToken!!, currentItemId)
//                        .observe(viewLifecycleOwner) { result ->
//                            when (result) {
//                                "deleted thread" -> {
//                                    viewModel.refreshThreadsLiveData()
//                                        .observe<Resource<List<NetworkForumThreadCustom>>>(viewLifecycleOwner) { resource ->
//                                            refresh(resource)
//                                        }
//                                    showSnackbarWithAction(requireView(), "Deleted $titleThread")
//                                }
//                                else -> showSnackbarWithAction(
//                                    requireView(),
//                                    "An error has occurred! Please try again!"
//                                )
//                            }
//                        }
//                }
//            }
//            REPORT_ITEM_ORDER -> TODO("report thread")
//        }
//        return true
//    }

    private fun refresh(resource: Resource<List<NetworkForumThreadCustom>>) {
        when (resource) {
            is Resource.Success -> {
                binding.progressBar.visibility = View.GONE
                adapter.submitList(resource.data?.sortedByDescending { forum ->
                    forum.createdAt
                })
            }
            is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
            is Resource.Error -> {
                Snackbar
                    .make(requireView(), "${resource.message}", Snackbar.LENGTH_LONG)
                    .show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    companion object {
        const val EDIT_ITEM_ORDER = 0
        const val DELETE_ITEM_ORDER = 1
        const val REPORT_ITEM_ORDER = 2
    }
}

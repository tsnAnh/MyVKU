/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.my_vku.thread

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ThreadAdapter
import dev.tsnanh.vku.adapters.ThreadClickListener
import dev.tsnanh.vku.databinding.FragmentThreadBinding
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.viewmodels.my_vku.ThreadViewModel
import dev.tsnanh.vku.viewmodels.my_vku.ThreadViewModelFactory
import timber.log.Timber

class ThreadFragment : Fragment() {

    private lateinit var viewModel: ThreadViewModel
    private lateinit var binding: FragmentThreadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()
        sharedElementReturnTransition = MaterialContainerTransform()
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

        val args: ThreadFragmentArgs by navArgs()
        binding.toolbar.title = args.title
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navArgs: ThreadFragmentArgs by navArgs()
        binding.layoutThread.transitionName = navArgs.id

        viewModel = ViewModelProvider(
            this,
            ThreadViewModelFactory(navArgs.id)
        ).get(ThreadViewModel::class.java)

        configureList()
        val adapter = ThreadAdapter(ThreadClickListener { thread, cardView ->
            viewModel.onNavigateToReplies(thread, cardView)
            Timber.d("ThreadClickListener called: $thread")
        })
        binding.listThread.adapter = adapter

        viewModel.threads.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Resource.Success -> {
                        adapter.submitList(it.data!!)
                        binding.progressBar.visibility = View.GONE
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
        })

        viewModel.forum.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.forum = it.data
            }
        })

        viewModel.navigateToReplies.observe(viewLifecycleOwner, Observer {
            it?.let {
                val extras = FragmentNavigatorExtras(
                    it.second to it.first.id
                )
                findNavController().navigate(
                    ThreadFragmentDirections.actionNavigationThreadToNavigationReplies(
                        it.first.id,
                        it.first.title
                    ),
                    extras
                )
                viewModel.onNavigatedToReplies()
            }
        })

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
        binding.listThread.setHasFixedSize(true)
        binding.listThread.layoutManager = LinearLayoutManager(requireContext())
    }

}

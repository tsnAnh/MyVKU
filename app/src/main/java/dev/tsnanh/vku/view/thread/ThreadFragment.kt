/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.view.thread

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ThreadAdapter
import dev.tsnanh.vku.adapters.ThreadClickListener
import dev.tsnanh.vku.databinding.FragmentThreadBinding
import dev.tsnanh.vku.domain.Resource
import timber.log.Timber

class ThreadFragment : Fragment() {

    private lateinit var viewModel: ThreadViewModel
    private lateinit var binding: FragmentThreadBinding

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
            .inflate(inflater, R.layout.fragment_thread, container, false)

        val args: ThreadFragmentArgs by navArgs()
        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            title = args.title
        }

        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val navArgs: ThreadFragmentArgs by navArgs()

        viewModel = ViewModelProvider(
            this,
            ThreadViewModelFactory(navArgs.id)
        ).get(ThreadViewModel::class.java)

        configureList()
        val adapter = ThreadAdapter(ThreadClickListener {
            viewModel.onNavigateToReplies(it)
            Timber.d("ThreadClickListener called: $it")
        })
        binding.listThread.adapter = adapter

        viewModel.threads.observe(viewLifecycleOwner, Observer {
            it?.let {
                when (it) {
                    is Resource.Success -> {
                        adapter.submitList(it.data)
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
                binding.forum = it
            }
        })

        viewModel.navigateToReplies.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    ThreadFragmentDirections
                        .actionNavigationThreadToNavigationReplies(it.id, it.title)
                )
                viewModel.onNavigatedToReplies()
            }
        })

        binding.fabNew.setOnClickListener {
            findNavController().navigate(
                ThreadFragmentDirections
                    .actionNavigationThreadToNavigationNewThread(navArgs.id, navArgs.title)
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

    override fun onStart() {
        viewModel.refreshThreads()
        super.onStart()
    }
}

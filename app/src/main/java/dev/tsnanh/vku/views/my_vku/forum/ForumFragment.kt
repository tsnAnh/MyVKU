/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.my_vku.forum

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialFadeThrough
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ForumAdapter
import dev.tsnanh.vku.adapters.ForumClickListener
import dev.tsnanh.vku.databinding.FragmentForumBinding
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.showSnackbarWithAction
import dev.tsnanh.vku.viewmodels.my_vku.ForumViewModel

class ForumFragment : Fragment() {

    private lateinit var binding: FragmentForumBinding
    private val viewModel: ForumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = Hold()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_forum, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.listTopics.setHasFixedSize(true)
        val rotation =
            (requireContext()
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.rotation
        if (rotation == 0) {
            binding.listTopics.layoutManager = LinearLayoutManager(requireContext())
        } else {
            binding.listTopics.layoutManager = GridLayoutManager(requireContext(), 2)
        }

        val adapter = ForumAdapter(emptyList(), ForumClickListener { forum, imageView ->
            viewModel.onItemClick(forum to imageView)
        })

        binding.listTopics.adapter = adapter

        viewModel.forums.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.errorLayout.visibility = View.GONE
                when (it) {
                    is Resource.Success -> {
                        adapter.updateForums(it.data?.forums!!)
                        binding.progressBar.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {
                        binding.errorLayout.visibility = View.VISIBLE
                        showSnackbarWithAction(
                            requireView(),
                            it.message.toString(),
                            requireContext().getString(R.string.text_hide)
                        )
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        })

        viewModel.navigateToListThread.observe(viewLifecycleOwner, Observer {
            it?.let {
                val extras = FragmentNavigatorExtras(
                    it.second to it.first.id
                )
                findNavController().navigate(
                    ForumFragmentDirections.actionNavigationForumToNavigationThread(
                        it.first.id,
                        it.first.title
                    ),
                    extras
                )
                viewModel.onItemClicked()
            }
        })

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

//        binding.swipeToRefresh.setOnRefreshListener {
//            lifecycleScope.launch {
//                viewModel.refreshForums()
//                binding.swipeToRefresh.isRefreshing = false
//            }
//        }

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

    override fun onResume() {
        super.onResume()
        binding.fabNewThread.show()
    }

}

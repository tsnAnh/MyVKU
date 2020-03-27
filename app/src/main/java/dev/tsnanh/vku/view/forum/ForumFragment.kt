package dev.tsnanh.vku.view.forum

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.ForumAdapter
import dev.tsnanh.vku.adapters.ForumClickListener
import dev.tsnanh.vku.database.VKUDatabase
import dev.tsnanh.vku.databinding.FragmentForumBinding

class ForumFragment : Fragment() {

    private lateinit var binding: FragmentForumBinding
    private lateinit var viewModel: ForumViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_forum, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val database = VKUDatabase.getInstance(requireActivity().application)
        val factory = ForumViewModelFactory(database)

        viewModel = ViewModelProvider(this, factory).get(ForumViewModel::class.java)

        binding.listTopics.setHasFixedSize(true)
        binding.listTopics.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ForumAdapter(ForumClickListener {
            viewModel.onItemClick(it)
        })

        binding.listTopics.adapter = adapter

        viewModel.forums.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToListThread.observe(viewLifecycleOwner, Observer {
            it?.let {
                viewModel.onItemClicked()
            }
        })
    }

}

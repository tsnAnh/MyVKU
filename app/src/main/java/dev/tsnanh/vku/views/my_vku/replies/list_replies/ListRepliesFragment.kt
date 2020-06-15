package dev.tsnanh.vku.views.my_vku.replies.list_replies

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
import dev.tsnanh.vku.adapters.RepliesAdapter
import dev.tsnanh.vku.databinding.FragmentListRepliesBinding
import dev.tsnanh.vku.domain.entities.Resource
import timber.log.Timber

class ListRepliesFragment(private val threadId: String, private val position: Int) : Fragment() {

    private lateinit var viewModel: ListRepliesViewModel
    private lateinit var binding: FragmentListRepliesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_replies, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            ListRepliesViewModelFactory(
                threadId,
                position
            )
        ).get(
            ListRepliesViewModel::class.java
        )

        binding.lifecycleOwner = viewLifecycleOwner

        val adapterReplies = RepliesAdapter()

        binding.listReplies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = adapterReplies
        }

        viewModel.listReplies.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                }
                is Resource.Success -> {
                    Timber.d(result.data?.replies.toString())
                    adapterReplies.submitList(result.data?.replies)
                }
            }
        })
    }

}

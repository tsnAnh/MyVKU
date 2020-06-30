package dev.tsnanh.vku.views.my_vku.list_replies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.RepliesAdapter
import dev.tsnanh.vku.adapters.ReplyClickListener
import dev.tsnanh.vku.databinding.FragmentListRepliesBinding
import dev.tsnanh.vku.domain.entities.NetworkCustomReply
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.viewmodels.my_vku.ListRepliesViewModel
import dev.tsnanh.vku.viewmodels.my_vku.ListRepliesViewModelFactory
import dev.tsnanh.vku.views.my_vku.reply.ReplyFragmentDirections
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

        val adapterReplies = RepliesAdapter(
            ReplyClickListener(
                reply = replyClickListener,
                share = shareClickListener
            )
        )

        binding.listReplies.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = adapterReplies
            itemAnimator = null
            adapter?.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
        }

        viewModel.listReplies.observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                }
                is Resource.Success -> {
                    binding.swipeToRefresh.isRefreshing = false
                    adapterReplies.submitList(result.data?.replies)
                    Timber.i("Yo")
                }
            }
        })

        binding.swipeToRefresh.setOnRefreshListener {
            viewModel.refreshPage()
        }
    }

    private val replyClickListener: (NetworkCustomReply) -> Unit = { reply ->
        parentFragment?.findNavController()?.navigate(
            ReplyFragmentDirections.actionNavigationRepliesToNewReplyFragment(
                reply.threadId,
                reply.id
            )
        )
    }
    private val shareClickListener: (NetworkCustomReply) -> Unit = { reply ->
//        findNavController().navigate(L)
    }

}

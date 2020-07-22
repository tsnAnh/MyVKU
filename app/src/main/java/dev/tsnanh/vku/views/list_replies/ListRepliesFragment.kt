package dev.tsnanh.vku.views.list_replies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.RepliesAdapter
import dev.tsnanh.vku.adapters.ReplyClickListener
import dev.tsnanh.vku.databinding.FragmentListRepliesBinding
import dev.tsnanh.vku.domain.entities.NetworkCustomReply
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.viewmodels.ListRepliesViewModel
import dev.tsnanh.vku.viewmodels.ListRepliesViewModelFactory
import dev.tsnanh.vku.views.reply.ReplyFragment.Companion.DELETE_ITEM_ORDER
import dev.tsnanh.vku.views.reply.ReplyFragment.Companion.EDIT_ITEM_ORDER
import dev.tsnanh.vku.views.reply.ReplyFragment.Companion.REPORT_ITEM_ORDER
import dev.tsnanh.vku.views.reply.ReplyFragmentDirections
import org.koin.java.KoinJavaComponent

class ListRepliesFragment(
    private val threadId: String,
    private val position: Int,
    private val isScrollDown: Boolean
) : Fragment() {

    private lateinit var viewModel: ListRepliesViewModel
    private lateinit var binding: FragmentListRepliesBinding
    private lateinit var adapterReplies: RepliesAdapter
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

        adapterReplies = RepliesAdapter(
            GoogleSignIn.getLastSignedInAccount(requireContext())?.id!!,
            ReplyClickListener(
                reply = replyClickListener,
                share = shareClickListener
            )
        )

        binding.listReplies.apply {
            layoutManager = LinearLayoutManager(requireContext()).apply {
                stackFromEnd = isScrollDown
            }
            setHasFixedSize(true)
            adapter = adapterReplies
            itemAnimator = null
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

    override fun onResume() {
        super.onResume()
        viewModel.refreshPage()
    }

    private val shareClickListener: (NetworkCustomReply) -> Unit = {
//        findNavController().navigate(L)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val currentReplyId = adapterReplies.currentList[item.itemId].id
        val userDisplayName = adapterReplies.currentList[item.itemId].uid?.displayName
        val client by KoinJavaComponent.inject(GoogleSignInClient::class.java)

        when (item.order) {
            EDIT_ITEM_ORDER -> {
                parentFragment?.findNavController()?.navigate(
                    ReplyFragmentDirections.actionNavigationRepliesToNavigationUpdateReply(
                        currentReplyId
                    )
                )
            }
            DELETE_ITEM_ORDER -> {
                TODO("delete reply and reload")
            }
            REPORT_ITEM_ORDER -> {
                // TODO: 20/07/2020 implement later
            }
        }
        return true
    }
}

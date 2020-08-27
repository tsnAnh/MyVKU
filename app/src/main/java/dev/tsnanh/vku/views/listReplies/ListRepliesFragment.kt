package dev.tsnanh.vku.views.listReplies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.RepliesAdapter
import dev.tsnanh.vku.adapters.ReplyClickListener
import dev.tsnanh.vku.databinding.FragmentListRepliesBinding
import dev.tsnanh.vku.domain.entities.NetworkCustomReply
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.viewmodels.ListRepliesViewModel
import dev.tsnanh.vku.views.reply.ReplyFragment.Companion.DELETE_ITEM_ORDER
import dev.tsnanh.vku.views.reply.ReplyFragment.Companion.EDIT_ITEM_ORDER
import dev.tsnanh.vku.views.reply.ReplyFragment.Companion.REPORT_ITEM_ORDER
import dev.tsnanh.vku.views.reply.ReplyFragmentDirections

@AndroidEntryPoint
class ListRepliesFragment(
    private val threadId: String,
    private val position: Int,
    private val isScrollDown: Boolean
) : Fragment() {
    private val viewModel: ListRepliesViewModel by viewModels()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        viewModel.refreshPage(threadId, position).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Resource.Loading -> {
                }
                is Resource.Error -> {
                }
                is Resource.Success -> {
                    adapterReplies.submitList(result.data?.replies)
                }
            }
        })
    }

    private val replyClickListener: (NetworkCustomReply) -> Unit = { reply ->
        parentFragment?.findNavController()?.navigate(
            ReplyFragmentDirections.actionNavigationRepliesToNewReplyFragment(
                reply.threadId,
                reply.id
            )
        )
    }

    private val shareClickListener: (NetworkCustomReply) -> Unit = {
//        findNavController().navigate(L)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val currentReplyId = adapterReplies.currentList[item.itemId].id
        val userDisplayName = adapterReplies.currentList[item.itemId].uid?.displayName

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

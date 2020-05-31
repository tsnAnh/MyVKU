package dev.tsnanh.vku.views.replies.list_replies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.FragmentListRepliesBinding

class ListRepliesFragment(private val threadId: String, private val position: Int) : Fragment() {

    private val viewModel: ListRepliesViewModel by viewModels()
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

        Toast.makeText(requireContext(), position.toString(), Toast.LENGTH_LONG).show()
    }

}

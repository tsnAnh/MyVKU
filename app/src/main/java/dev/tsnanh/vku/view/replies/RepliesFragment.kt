package dev.tsnanh.vku.view.replies

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
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.FragmentRepliesBinding
import timber.log.Timber

class RepliesFragment : Fragment() {

    private lateinit var binding: FragmentRepliesBinding
    private lateinit var viewModel: RepliesViewModel

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
            .inflate(inflater, R.layout.fragment_replies, container, false)

        binding.bottomAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navArgs: RepliesFragmentArgs by navArgs()
        Timber.d(navArgs.threadId)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(
            this,
            RepliesViewModelFactory(navArgs.threadId)
        ).get(RepliesViewModel::class.java)

        viewModel.replies.observe(viewLifecycleOwner, Observer {
            it?.let {

            }
        })
    }

}

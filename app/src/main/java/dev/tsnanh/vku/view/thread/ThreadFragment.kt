package dev.tsnanh.vku.view.thread

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.FragmentThreadBinding

class ThreadFragment : Fragment() {

    private val viewModel: ThreadViewModel by viewModels()
    private lateinit var binding: FragmentThreadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_thread, container, false)

        return binding.root
    }

}

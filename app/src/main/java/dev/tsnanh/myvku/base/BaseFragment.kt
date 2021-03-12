package dev.tsnanh.myvku.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough

abstract class BaseFragment<DB : ViewDataBinding> : Fragment() {
    protected lateinit var binding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = initDataBinding(inflater, container)
        binding.initViews()
        return binding.root
    }

    protected abstract fun initDataBinding(inflater: LayoutInflater, container: ViewGroup?): DB

    protected abstract fun DB.initViews()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        lifecycleScope.launchWhenStarted {
            observeData()
        }
    }

    protected abstract fun observeData()
}

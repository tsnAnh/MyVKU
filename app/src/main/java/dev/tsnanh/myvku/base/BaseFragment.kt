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
import kotlinx.coroutines.Job

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {
    protected abstract val viewModel: VM
    protected lateinit var binding: DB
    protected val jobs = mutableListOf<Job>()

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
        jobs.add(
            lifecycleScope.launchWhenStarted {
                viewModel.observeData()
            }
        )
    }

    protected abstract suspend fun VM.observeData()

    override fun onDestroyView() {
        super.onDestroyView()
        jobs.forEach(Job::cancel)
    }
}

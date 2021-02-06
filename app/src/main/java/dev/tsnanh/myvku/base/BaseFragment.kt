package dev.tsnanh.myvku.base

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.Job

abstract class BaseFragment : Fragment() {
    protected abstract val viewModel: BaseViewModel
    protected val jobs = mutableListOf<Job>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }

        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        bindView()
    }

    abstract fun setupView()

    abstract fun bindView()

    override fun onDestroyView() {
        super.onDestroyView()
        jobs.forEach(Job::cancel)
    }
}
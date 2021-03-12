package dev.tsnanh.myvku.views.news.pages.absence

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.databinding.FragmentPageNewsBinding
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.views.news.pages.BaseNewsPageFragment
import dev.tsnanh.myvku.views.news.pages.absence.adapter.AbsenceAdapter
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PageAbsenceFragment : BaseNewsPageFragment() {
    companion object {
        fun newInstance() = PageAbsenceFragment()
    }

    private val viewModel: PageAbsenceViewModel by viewModels()
    private lateinit var adapterAbsence: AbsenceAdapter

    private fun showLayout(messageString: String, drawable: Int) {
        with(binding.layoutNoItem) {
            message.text = messageString
            image.setImageResource(drawable)
            root.isVisible = true
        }
    }

    override fun FragmentPageNewsBinding.initViews() {
        super.initViews()
        adapterAbsence = AbsenceAdapter().also {
            list.adapter = it
        }
    }

    override fun observeData() {
        lifecycleScope.launchWhenStarted {
            viewModel.absences.collect { state ->
                when (state) {
                    is State.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is State.Error -> {
                        println("Error: ${state.throwable?.localizedMessage}")
                    }
                    is State.Success -> {
                        binding.progressBar.isVisible = false
                        val absences = state.data
                        if (absences != null && absences.isNotEmpty()) {
                            adapterAbsence.submitList(state.data)
                        } else {
                            showLayout(
                                requireContext().getString(R.string.text_no_absences_here),
                                R.drawable.empty
                            )
                        }
                    }
                }
            }
        }
    }
}

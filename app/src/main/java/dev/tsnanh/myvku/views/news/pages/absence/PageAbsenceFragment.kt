package dev.tsnanh.myvku.views.news.pages.absence

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.databinding.FragmentPageNewsBinding
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.views.news.pages.absence.adapter.AbsenceAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PageAbsenceFragment : Fragment() {
    companion object {
        fun newInstance() = PageAbsenceFragment()
    }

    private lateinit var binding: FragmentPageNewsBinding
    private val viewModel: PageAbsenceViewModel by viewModels()
    private lateinit var adapterAbsence: AbsenceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_news, container, false)
        return binding.root
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        adapterAbsence = AbsenceAdapter()

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = adapterAbsence
        }

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
                        with(binding) {
                            swipeToRefresh.isRefreshing = false
                            progressBar.isVisible = false
                        }
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

    private fun showLayout(messageString: String, drawable: Int) {
        with(binding.layoutNoItem) {
            message.text = messageString
            image.setImageResource(drawable)
            root.isVisible = true
        }
    }
}

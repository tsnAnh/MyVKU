package dev.tsnanh.myvku.views.news.pages.makeup

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
import dev.tsnanh.myvku.views.news.pages.makeup.adapter.MakeupClassAdapter
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PageMakeupClassFragment : Fragment() {
    companion object {
        fun newInstance() = PageMakeupClassFragment()
    }

    private lateinit var binding: FragmentPageNewsBinding
    private lateinit var adapterMakeupClass: MakeupClassAdapter
    private val viewModel: PageMakeupClassViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_page_news, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        adapterMakeupClass = MakeupClassAdapter()

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = adapterMakeupClass
        }

        lifecycleScope.launchWhenStarted {
            viewModel.makeUpClass
                .collect { state ->
                    when (state) {
                        is State.Error -> {
                            println("Error: ${state.throwable?.localizedMessage}")
                        }
                        is State.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        is State.Success -> {
                            with(binding) {
                                swipeToRefresh.isRefreshing = false
                                progressBar.isVisible = false
                            }
                            val makeupClasses = state.data
                            if (makeupClasses != null && makeupClasses.isNotEmpty()) {
                                adapterMakeupClass.submitList(state.data)
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

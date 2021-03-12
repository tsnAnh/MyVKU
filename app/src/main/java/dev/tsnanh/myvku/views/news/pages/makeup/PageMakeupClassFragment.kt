package dev.tsnanh.myvku.views.news.pages.makeup

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.databinding.FragmentPageNewsBinding
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.views.news.pages.BaseNewsPageFragment
import dev.tsnanh.myvku.views.news.pages.makeup.adapter.MakeupClassAdapter
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class PageMakeupClassFragment : BaseNewsPageFragment() {
    companion object {
        fun newInstance() = PageMakeupClassFragment()
    }

    private lateinit var adapterMakeupClass: MakeupClassAdapter
    private val viewModel: PageMakeupClassViewModel by viewModels()

    override fun FragmentPageNewsBinding.initViews() {
        super.initViews()
        adapterMakeupClass = MakeupClassAdapter()
        list.adapter = adapterMakeupClass
    }

    override fun observeData() {
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

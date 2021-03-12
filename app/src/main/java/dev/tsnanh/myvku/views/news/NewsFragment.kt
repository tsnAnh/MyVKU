/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.databinding.FragmentNewsBinding
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.math.abs
import kotlin.math.max

private const val MIN_SCALE = 0.85f
private const val MIN_ALPHA = 0.5f

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NewsFragment : BaseFragment<FragmentNewsBinding>() {
    private val viewModel: NewsViewModel by viewModels()
    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ) = FragmentNewsBinding.inflate(inflater, container, false)

    override fun FragmentNewsBinding.initViews() {
        binding.pager.apply {
            adapter = NewsPagerAdapter(this@NewsFragment)
            // TODO: 8/28/2020 make material shared axis page transformer
            setPageTransformer { page, position ->
                page.apply {
                    val pageWidth = width
                    val pageHeight = height
                    when {
                        position < -1 -> { // [-Infinity,-1)
                            // This page is way off-screen to the left.
                            alpha = 0f
                        }
                        position <= 1 -> { // [-1,1]
                            // Modify the default slide transition to shrink the page as well
                            val scaleFactor = max(MIN_SCALE, 1 - abs(position))
                            val vertMargin = pageHeight * (1 - scaleFactor) / 2
                            val horzMargin = pageWidth * (1 - scaleFactor) / 2
                            translationX = if (position < 0) {
                                horzMargin - vertMargin / 2
                            } else {
                                horzMargin + vertMargin / 2
                            }

                            // Scale the page down (between MIN_SCALE and 1)
                            scaleX = scaleFactor
                            scaleY = scaleFactor

                            // Fade the page relative to its size.
                            alpha = (
                                MIN_ALPHA +
                                    (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA))
                                )
                        }
                        else -> { // (1,+Infinity]
                            // This page is way off-screen to the right.
                            alpha = 0f
                        }
                    }
                }
            }
        }

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> requireContext().getString(R.string.text_news)
                1 -> requireContext().getString(R.string.text_absences)
                2 -> requireContext().getString(R.string.text_makeup_classes)
                else -> requireContext().getString(R.string.text_confession)
            }
        }.attach()
    }

    override fun observeData() {
        binding.viewModel = viewModel
    }
}

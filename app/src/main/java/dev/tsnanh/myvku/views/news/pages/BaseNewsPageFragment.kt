package dev.tsnanh.myvku.views.news.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.databinding.FragmentPageNewsBinding
import dev.tsnanh.myvku.utils.isMPlus

abstract class BaseNewsPageFragment : BaseFragment<FragmentPageNewsBinding>() {
    override fun initDataBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPageNewsBinding {
        return FragmentPageNewsBinding.inflate(inflater, container, false)
    }

    fun initViews() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner

            scrollToTop.translationX = 256F
            scrollToTop.setOnClickListener { list.smoothScrollToPosition(0) }
            list.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                if (isMPlus()) {
                    setOnScrollChangeListener { v, _, _, _, _ ->
                        showScrollToTopButton((v as RecyclerView).computeVerticalScrollOffset() > 2000)
                    }
                } else {
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrollStateChanged(
                            recyclerView: RecyclerView,
                            newState: Int
                        ) {
                            showScrollToTopButton(recyclerView.computeVerticalScrollOffset() > 2000)
                        }
                    })
                }
            }
        }
    }

    override fun observeData() {
        // For rent
    }

    private fun showScrollToTopButton(isVisible: Boolean) {
        binding.scrollToTop.animate()
            .translationX(if (isVisible) 0F else 256F)
            .setDuration(300L)
            .setListener(null)
    }
}
package dev.tsnanh.myvku.views.news.pages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.databinding.FragmentPageNewsBinding

abstract class BaseNewsPageFragment : BaseFragment<FragmentPageNewsBinding>() {
    override fun initDataBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPageNewsBinding {
        return FragmentPageNewsBinding.inflate(inflater, container, false)
    }

    fun initViews() {
        with(binding) {
            lifecycleOwner = viewLifecycleOwner

            listNews.list.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
    }

    override fun observeData() {
        // For rent
    }
}
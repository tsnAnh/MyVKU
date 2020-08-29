package dev.tsnanh.vku.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.tsnanh.vku.views.ListRepliesFragment
import javax.inject.Inject

class ListRepliesPagerAdapter @Inject constructor(
    fragment: Fragment,
    private val threadId: String,
    private val totalPage: Int
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = totalPage

    override fun createFragment(position: Int) = if (position != totalPage) {
        ListRepliesFragment(
            threadId,
            position + 1,
            false
        )
    } else {
        ListRepliesFragment(
            threadId, position + 1, position != 1
        )
    }
}
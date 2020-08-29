package dev.tsnanh.vku.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.tsnanh.vku.views.PageAbsenceFragment
import dev.tsnanh.vku.views.PageMakeupClassFragment
import dev.tsnanh.vku.views.PageNewsFragment

class NewsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int) = when (position) {
        0 -> PageNewsFragment.newInstance()
        1 -> PageAbsenceFragment.newInstance()
        else -> PageMakeupClassFragment.newInstance()
    }
}
package dev.tsnanh.myvku.views.news

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import dev.tsnanh.myvku.views.news.pages.absence.PageAbsenceFragment
import dev.tsnanh.myvku.views.news.pages.makeup.PageMakeupClassFragment
import dev.tsnanh.myvku.views.news.pages.news.PageNewsFragment

class NewsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int) = when (position) {
        0 -> PageNewsFragment.newInstance()
        1 -> PageAbsenceFragment.newInstance()
        else -> PageMakeupClassFragment.newInstance()
    }
}
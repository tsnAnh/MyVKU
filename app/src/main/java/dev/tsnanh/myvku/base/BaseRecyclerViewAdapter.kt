package dev.tsnanh.myvku.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, VH : RecyclerView.ViewHolder>(
    diffUtilItemCallback: DiffUtil.ItemCallback<T>,
    private val onListChangeListener: OnListStateChangeListener
) : ListAdapter<T, VH>(diffUtilItemCallback) {
    override fun submitList(list: MutableList<T>?) {
        super.submitList(list)
        if (list.isNullOrEmpty()) {
            onListChangeListener.onListEmpty()
        } else {
            onListChangeListener.onListHasData()
        }
    }
}

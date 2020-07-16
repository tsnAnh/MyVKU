package dev.tsnanh.vku.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.vku.domain.entities.Notification
import dev.tsnanh.vku.viewholders.NotificationViewHolder

class NotificationAdapter(
    private val listener: NotificationClickListener
) : ListAdapter<Notification, NotificationViewHolder>(NotificationDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NotificationViewHolder.from(parent)

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class NotificationClickListener(val clickListener: (Notification) -> Unit) {
    fun onClick(notification: Notification) = clickListener(notification)
}

class NotificationDiffUtil : DiffUtil.ItemCallback<Notification>() {
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem.id == newItem.id
    }
}
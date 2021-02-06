package dev.tsnanh.myvku.views.notification.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.tsnanh.myvku.domain.entities.Notification
import javax.inject.Inject

class NotificationAdapter @Inject constructor(
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
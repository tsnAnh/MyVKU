package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ItemNotificationBinding
import dev.tsnanh.vku.domain.entities.Notification
import dev.tsnanh.vku.domain.entities.NotificationTitle

class NotificationViewHolder private constructor(
    private val binding: ItemNotificationBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): NotificationViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemNotificationBinding.inflate(inflater, parent, false)
            return NotificationViewHolder(binding)
        }
    }

    fun bind(notification: Notification) {
        binding.notification = notification
        binding.notiMessage = when (notification.message.data.title) {
            NotificationTitle.MESSAGE_LIKE.value -> binding.root.context.getString(R.string.text_message_like)
            NotificationTitle.MESSAGE_TO_OWNER.value -> binding.root.context.getString(R.string.text_message_to_owner)
            NotificationTitle.MESSAGE_TO_OWNER_CUSTOM.value -> binding.root.context.getString(R.string.text_message_to_owner_custom)
            NotificationTitle.MESSAGE_TO_QUOTED_USER.value -> binding.root.context.getString(R.string.text_message_to_quoted_user)
            NotificationTitle.MESSAGE_TO_ALL_SUBSCRIBERS.value -> binding.root.context.getString(R.string.text_message_to_all_subscribers)
            else -> throw IllegalArgumentException("WTF is this message???")
        }
        Glide.with(binding.imageView5).load(notification.message.data.uid.photoURL).centerCrop()
            .circleCrop()
            .into(binding.imageView5)
        binding.executePendingBindings()
    }
}
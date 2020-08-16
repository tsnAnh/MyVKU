package dev.tsnanh.vku.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.ItemNoticeBinding
import dev.tsnanh.vku.domain.entities.Absence
import dev.tsnanh.vku.domain.entities.MakeUpClass
import dev.tsnanh.vku.domain.entities.Notice

class NoticeViewHolder private constructor(
    private val binding: ItemNoticeBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun from(parent: ViewGroup): NoticeViewHolder {
            val binding =
                ItemNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return NoticeViewHolder(binding)
        }
    }

    fun bind(notice: Notice) {
        with(binding) {
            when (notice) {
                is Absence -> {
                    noticeType.text = root.context.getString(R.string.text_absence)
                    className.text = root.context.getString(
                        R.string.absence_class_name,
                        notice.className,
                        notice.dateNotice.substringBeforeLast(' ')
                    )
                    teacher.text = root.context.getString(
                        R.string.title_and_full_name,
                        notice.title,
                        "${notice.lastName} ${notice.firstName}"
                    )
                }
                is MakeUpClass -> {
                    noticeType.text = root.context.getString(R.string.text_makeup_class)
                    className.text = root.context.getString(
                        R.string.makeup_class_name,
                        notice.className,
                        notice.dateMakeUp
                    )
                    teacher.text = root.context.getString(
                        R.string.title_and_full_name,
                        notice.title,
                        "${notice.lastName} ${notice.firstName}"
                    )
                }
            }
        }
    }
}
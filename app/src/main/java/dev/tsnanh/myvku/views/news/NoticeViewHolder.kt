package dev.tsnanh.myvku.views.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.databinding.ItemNoticeBinding
import dev.tsnanh.myvku.domain.entities.Absence
import dev.tsnanh.myvku.domain.entities.MakeUpClass
import dev.tsnanh.myvku.domain.entities.Notice

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
                    noticeType.text = root.context.getString(R.string.text_absences)
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
                    noticeType.text = root.context.getString(R.string.text_makeup_classes)
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

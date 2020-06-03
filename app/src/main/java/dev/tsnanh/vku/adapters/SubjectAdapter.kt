package dev.tsnanh.vku.adapters
//
//import android.view.ViewGroup
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import dev.tsnanh.vku.domain.Subject
//import dev.tsnanh.vku.viewholders.SubjectViewHolder
//
//class SubjectAdapter(
//    private val clickListener: SubjectClickListener
//) : ListAdapter<Subject, SubjectViewHolder>(SubjectDiffUtil()) {
//    override fun onCreateViewHoldereateViewHolder(parent: ViewGroup, viewType: Int) =
//        SubjectViewHolder.from(parent)
//
//    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
//        holder.bind(getItem(position), clickListener)
//    }
//}
//
//class SubjectClickListener(
//    val setAlarmClickListener: (Subject) -> Unit,
//    val attendanceClickListener: (Subject) -> Unit
//) {
//    fun onSetAlarmClickListener(subject: Subject) = setAlarmClickListener(subject)
//    fun onAttendanceClickListener(subject: Subject) = attendanceClickListener(subject)
//}
//
//class SubjectDiffUtil : DiffUtil.ItemCallback<Subject>() {
//    override fun areItemsTheSame(oldItem: Subject, newItem: Subject) = oldItem === newItem
//
//    override fun areContentsTheSame(oldItem: Subject, newItem: Subject) = oldItem.id == newItem.id
//}
package dev.tsnanh.myvku.views.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.databinding.FragmentAttendanceBinding

class AttendanceFragment : BaseFragment<AttendanceViewModel, FragmentAttendanceBinding>() {
    override val viewModel: AttendanceViewModel by viewModels()

    override fun initDataBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAttendanceBinding.inflate(inflater, container, false)

    override fun FragmentAttendanceBinding.initViews() {
    }

    override suspend fun AttendanceViewModel.observeData() {
    }
}

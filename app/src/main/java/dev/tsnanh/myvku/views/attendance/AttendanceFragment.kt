package dev.tsnanh.myvku.views.attendance

import android.view.LayoutInflater
import android.view.ViewGroup
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.databinding.FragmentAttendanceBinding

class AttendanceFragment : BaseFragment<FragmentAttendanceBinding>() {
    override fun initDataBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAttendanceBinding.inflate(inflater, container, false)

    override fun FragmentAttendanceBinding.initViews() {
    }

    override fun observeData() {
    }
}

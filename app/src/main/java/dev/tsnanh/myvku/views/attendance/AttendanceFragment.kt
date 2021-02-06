package dev.tsnanh.myvku.views.attendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.databinding.FragmentAttendanceBinding

class AttendanceFragment : BaseFragment() {
    private lateinit var binding: FragmentAttendanceBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_attendance, container, false)

        return binding.root
    }

    override val viewModel: AttendanceViewModel by viewModels()

    override fun setupView() {
    }

    override fun bindView() {
    }
}
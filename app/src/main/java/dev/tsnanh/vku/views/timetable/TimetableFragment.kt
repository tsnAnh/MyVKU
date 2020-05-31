/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.SubjectAdapter
import dev.tsnanh.vku.adapters.SubjectClickListener
import dev.tsnanh.vku.databinding.FragmentTimetableBinding

class TimetableFragment : Fragment() {

    private lateinit var binding: FragmentTimetableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_timetable, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = SubjectAdapter(SubjectClickListener(
            setAlarmClickListener = {

            },
            attendanceClickListener = {}
        ))
        binding.listSubjects.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            setAdapter(adapter)
        }
    }

}

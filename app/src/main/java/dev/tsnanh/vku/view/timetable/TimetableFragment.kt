/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.view.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.transition.MaterialFadeThrough
import dev.tsnanh.vku.R
import dev.tsnanh.vku.databinding.FragmentTimetableBinding

class TimetableFragment : Fragment() {

    private val viewModel: TimetableViewModel by viewModels()
    private lateinit var binding: FragmentTimetableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough.create()
        exitTransition = MaterialFadeThrough.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_timetable, container, false)

        return binding.root
    }

}

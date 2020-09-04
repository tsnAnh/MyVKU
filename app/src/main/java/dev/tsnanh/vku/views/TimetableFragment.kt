/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.TimetableAdapter
import dev.tsnanh.vku.adapters.TimetableClickListener
import dev.tsnanh.vku.databinding.FragmentTimetableBinding
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.getDayOfWeekFromString
import dev.tsnanh.vku.utils.getHourFromLesson
import dev.tsnanh.vku.utils.getMinutesFromStringLesson
import dev.tsnanh.vku.viewmodels.TimetableViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@AndroidEntryPoint
class TimetableFragment : Fragment() {
    private lateinit var binding: FragmentTimetableBinding

    @FlowPreview
    private val viewModel: TimetableViewModel by viewModels()

    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_timetable, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        val timetableAdapter = createTimetableAdapter()

        binding.listSubjects.apply {
            setHasFixedSize(false)
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(requireContext())
            adapter = timetableAdapter
        }

        viewModel.timetable.observe(viewLifecycleOwner) { result ->
            binding.progressBar.visibility = View.GONE
            timetableAdapter.submitList(result)
            // TODO: 02/09/2020 Scroll to subject position and highlight it
        }

        with(binding) {
            chipGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    allFilterChip.id -> viewModel.onFilterAll()
                    todayFilterChip.id -> viewModel.onFilterToday()
                    tomorrowFilterChip.id -> viewModel.onFilterTomorrow()
                }
            }
        }
    }

    private fun createTimetableAdapter(): TimetableAdapter {
        return TimetableAdapter(
            TimetableClickListener(
                setAlarmClickListener, comeInClassClickListener
            )
        )
    }

    private val setAlarmClickListener: (Subject) -> Unit = { subject ->
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(
                AlarmClock.EXTRA_MESSAGE,
                subject.className
            )
            putExtra(AlarmClock.EXTRA_HOUR, subject.lesson.getHourFromLesson())
            putExtra(AlarmClock.EXTRA_MINUTES, subject.lesson.getMinutesFromStringLesson())
            putExtra(AlarmClock.EXTRA_DAYS, arrayListOf(subject.dayOfWeek.getDayOfWeekFromString()))
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        }
        startActivity(intent)
        Snackbar.make(
            requireView(),
            requireContext().getString(R.string.msg_alarm_set),
            Snackbar.LENGTH_SHORT
        ).show()
    }
    private val comeInClassClickListener = { subject: Subject ->
        startActivity(Intent(Intent.ACTION_VIEW, Constants.ROOMS[subject.room]?.toUri()))
    }
}

/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.timetable

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.databinding.FragmentTimetableBinding
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.utils.Constants
import dev.tsnanh.myvku.utils.getDayOfWeekFromString
import dev.tsnanh.myvku.utils.getHourFromLesson
import dev.tsnanh.myvku.utils.getMinutesFromStringLesson
import dev.tsnanh.myvku.views.timetable.adapter.TimetableAdapter
import dev.tsnanh.myvku.views.timetable.adapter.TimetableClickListener
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class TimetableFragment : BaseFragment(), TimetableBindingHandler {
    private lateinit var binding: FragmentTimetableBinding

    override val viewModel: TimetableViewModel by viewModels()

    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_timetable, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@TimetableFragment.viewModel
            handler = this@TimetableFragment
        }

        val timetableAdapter = createTimetableAdapter()

        with(binding.listSubjects) {
            setHasFixedSize(false)
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(requireContext())
            adapter = timetableAdapter
        }
    }

    override fun setupView() {

    }

    override fun bindView() {
        jobs.add(
            lifecycleScope.launchWhenStarted {
                viewModel.user.collect { acc ->
                    binding.signIn.isVisible = acc == null
                }
            }
        )
    }

//    private suspend fun observe() {
//        viewModel.getTimetable()
//    }

    private fun createTimetableAdapter(): TimetableAdapter {
        return TimetableAdapter(
            TimetableClickListener(
                setAlarmClickListener, comeInClassClickListener
            )
        )
    }

    private val setAlarmClickListener: (Subject) -> Unit = { subject ->
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, subject.className)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (::binding.isInitialized) {
            outState.putInt("filterType", when (binding.chipGroup.checkedChipId) {
                R.id.all_filter_chip -> 0
                R.id.today_filter_chip -> 1
                R.id.tomorrow_filter_chip -> 2
                else -> 0
            })
        }
    }

    override fun signIn() {

    }
}

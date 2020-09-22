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
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import dev.tsnanh.vku.utils.*
import dev.tsnanh.vku.viewmodels.FilterType.*
import dev.tsnanh.vku.viewmodels.MainViewModel
import dev.tsnanh.vku.viewmodels.TimetableViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TimetableFragment : Fragment() {
    private lateinit var binding: FragmentTimetableBinding

    private val viewModel: TimetableViewModel by viewModels()

    private val activityViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var mGoogleSignInClient: GoogleSignInClient

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var isNetworkAvailable = false

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

        with(binding.listSubjects) {
            setHasFixedSize(false)
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(requireContext())
            adapter = timetableAdapter
        }

        viewModel.timetable.observe(viewLifecycleOwner) { (result, filterType) ->
            // hide progress bar
            binding.progressBar.visibility = View.GONE
            binding.swipeToRefresh.isRefreshing = false
            if (result.isNotEmpty()) {
                // display list subjects
                binding.layoutNoItem.root.isVisible = false
                binding.listSubjects.isVisible = true
                timetableAdapter.submitList(result)
            } else {
                // show no subject layout
                with(binding.layoutNoItem) {
                    when (filterType) {
                        ALL -> {
                            // Ignore
                        }
                        TODAY -> message.text = getString(R.string.text_no_subject_today)
                        TOMORROW -> message.text = getString(R.string.text_no_subjects_tomorow)
                    }
                    image.setImageResource(R.drawable.ic_round_toys_24)
                    root.isVisible = true
                }
                binding.listSubjects.isVisible = false
            }
            // TODO: 02/09/2020 Scroll to subject position and highlight it
        }

        viewModel.error.observe(viewLifecycleOwner) { t ->
            t?.let {
                when (t) {
                    is ConnectException -> {
                        binding.progressBar.isVisible = false
                    }
                    is SocketTimeoutException -> {
                        showSnackbar(requireView(), requireContext().getString(R.string.err_msg_request_timeout))
                    }
                    else -> {
                        Timber.e(t)
                        binding.swipeToRefresh.isRefreshing = false
                    }
                }
                viewModel.clearError()
            }
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

        activityViewModel.connectivityLiveData.observe(viewLifecycleOwner) { available ->
            isNetworkAvailable = available
            if (available) {
                viewModel.refresh()
            } else {
                view.post { showSnackbar(view, requireContext().getString(R.string.text_no_internet_connection)) }
            }
        }

        val filter = viewModel.filter

        if (filter != null) {
            filter.observe(viewLifecycleOwner) { value ->
                when (value) {
                    0 -> viewModel.onFilterAll()
                    1 -> viewModel.onFilterToday()
                    2 -> viewModel.onFilterTomorrow()
                }
            }
        } else {
            viewModel.onFilterAll()
        }

        binding.swipeToRefresh.setOnRefreshListener {
            with(viewModel) {
                if (isNetworkAvailable) {
                    refresh()
                } else {
                    binding.swipeToRefresh.isRefreshing = false
                    showSnackbar(view, requireContext().getString(R.string.text_no_internet_connection))
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
}

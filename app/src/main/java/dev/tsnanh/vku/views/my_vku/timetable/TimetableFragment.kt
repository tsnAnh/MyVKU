/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.views.my_vku.timetable

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import dev.tsnanh.vku.R
import dev.tsnanh.vku.adapters.TimetableAdapter
import dev.tsnanh.vku.adapters.TimetableClickListener
import dev.tsnanh.vku.databinding.FragmentTimetableBinding
import dev.tsnanh.vku.domain.entities.Subject
import dev.tsnanh.vku.utils.Constants
import dev.tsnanh.vku.utils.getDayOfWeekFromString
import dev.tsnanh.vku.utils.getHourFromStringLesson
import dev.tsnanh.vku.utils.getMinutesFromStringLesson
import dev.tsnanh.vku.viewmodels.my_vku.TimetableViewModel
import java.util.Calendar

class TimetableFragment : Fragment() {

    private lateinit var binding: FragmentTimetableBinding
    private val viewModel: TimetableViewModel by viewModels()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

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

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner

        val timetableAdapter = createTimetableAdapter()

        binding.listSubjects.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = timetableAdapter
        }
        val gso = GoogleSignInOptions.Builder()
            .requestId()
            .requestIdToken(getString(R.string.server_client_id))
            .requestProfile()
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        mGoogleSignInClient.silentSignIn().addOnCompleteListener {
            if (it.isSuccessful) {
                viewModel.timetable
                    .observe(viewLifecycleOwner, Observer { result ->
                        binding.progressBar.visibility = View.GONE
                        // FALSE[view current day only], TRUE[view all]
                        val viewAll = sharedPreferences.getBoolean(
                            requireContext().getString(R.string.show_all_subjects_key),
                            false
                        )
                        var list = result.sortedBy { subject ->
                            subject.lesson.length < 2
                        }
                        if (!viewAll) {
                            val dayOfWeek = Calendar.getInstance()[Calendar.DAY_OF_WEEK]
                            list = list.filter { subject ->
                                when (subject.dayOfWeek) {
                                    Constants.MONDAY -> Calendar.MONDAY == dayOfWeek
                                    Constants.TUESDAY -> Calendar.TUESDAY == dayOfWeek
                                    Constants.WEDNESDAY -> Calendar.WEDNESDAY == dayOfWeek
                                    Constants.THURSDAY -> Calendar.THURSDAY == dayOfWeek
                                    Constants.FRIDAY -> Calendar.FRIDAY == dayOfWeek
                                    Constants.SATURDAY -> Calendar.SATURDAY == dayOfWeek
                                    else -> Calendar.SUNDAY == dayOfWeek
                                }
                            }
                        }
                        timetableAdapter.updateSubjects(list)
                        // requireArguments().getString("subject", null)?.let {
                        //     var position = 0
                        //     list.filterIndexed { index, subject ->
                        //         position = index
                        //         subject.className == it
                        //     }
                        //     binding.listSubjects.scrollToPosition(position)
                        // }
                    })
                viewModel.refreshSubjects(it.getResult(ApiException::class.java)!!.email!!)
            }
        }

        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                val bar = Snackbar
                    .make(requireView(), it, Snackbar.LENGTH_INDEFINITE)
                bar.setAction(requireContext().getString(R.string.text_hide)) {
                    bar.dismiss()
                }
                bar.show()

                viewModel.onErrorDisplayed()
            }
        })
    }

    private fun createTimetableAdapter(): TimetableAdapter {
        return TimetableAdapter(
            emptyList(), TimetableClickListener(
                setAlarmClickListener, attendanceClickListener
            )
        )
    }

    private val setAlarmClickListener: (Subject) -> Unit = { subject ->
        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(
                AlarmClock.EXTRA_MESSAGE,
                subject.className
            )
            putExtra(AlarmClock.EXTRA_HOUR, subject.lesson.getHourFromStringLesson())
            putExtra(AlarmClock.EXTRA_MINUTES, subject.lesson.getMinutesFromStringLesson())
            putExtra(AlarmClock.EXTRA_DAYS, subject.dayOfWeek.getDayOfWeekFromString())
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        }
        startActivity(intent)
        Snackbar.make(
            requireView(),
            requireContext().getString(R.string.msg_alarm_set),
            Snackbar.LENGTH_SHORT
        ).show()
    }
    private val attendanceClickListener: (Subject) -> Unit = { subject ->
        Snackbar.make(
            requireView(),
            requireContext().getString(R.string.msg_alarm_set),
            Snackbar.LENGTH_SHORT
        ).show()
    }
}

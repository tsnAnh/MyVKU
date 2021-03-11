/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.timetable

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.provider.AlarmClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.base.BaseFragment
import dev.tsnanh.myvku.base.OnListStateChangeListener
import dev.tsnanh.myvku.databinding.FragmentTimetableBinding
import dev.tsnanh.myvku.domain.entities.State
import dev.tsnanh.myvku.domain.entities.Subject
import dev.tsnanh.myvku.utils.Constants
import dev.tsnanh.myvku.utils.getDayOfWeekFromString
import dev.tsnanh.myvku.utils.getHourFromLesson
import dev.tsnanh.myvku.utils.getMinutesFromStringLesson
import dev.tsnanh.myvku.utils.showSnackbar
import dev.tsnanh.myvku.views.main.MainViewModel
import dev.tsnanh.myvku.views.timetable.adapter.TimetableAdapter
import dev.tsnanh.myvku.views.timetable.adapter.TimetableClickListener
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

private const val GOOGLE_SIGN_IN_REQUEST_CODE = 0

@AndroidEntryPoint
class TimetableFragment :
    BaseFragment<TimetableViewModel, FragmentTimetableBinding>(),
    OnListStateChangeListener {
    private lateinit var timetableAdapter: TimetableAdapter
    override val viewModel: TimetableViewModel by viewModels()
    private val activityViewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val loginResultHandler =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            try {
                GoogleSignIn.getSignedInAccountFromIntent(it.data).addOnSuccessListener { user ->
                    activityViewModel.setUser(user)
                    viewModel.refresh()
                }.addOnFailureListener { e ->
                    println(e)
                    showSnackbar(binding.root, "Failed to sign in")
                }
            } catch (e: ApiException) {
                println(e)
                showSnackbar(binding.root, "Failed to sign in")
            }
        }

    private fun createTimetableAdapter(): TimetableAdapter {
        return TimetableAdapter(
            TimetableClickListener(
                setAlarmClickListener, comeInClassClickListener
            ),
            this
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

    override fun initDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentTimetableBinding {
        setHasOptionsMenu(true)
        return FragmentTimetableBinding.inflate(inflater, container, false)
    }

    override fun FragmentTimetableBinding.initViews() {
        with(this) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@TimetableFragment.viewModel
        }

        timetableAdapter = createTimetableAdapter()

        with(listSubjects) {
            setHasFixedSize(false)
            isNestedScrollingEnabled = true
            layoutManager = LinearLayoutManager(requireContext())
            adapter = timetableAdapter
        }
    }

    override suspend fun TimetableViewModel.observeData() {
        println("YESSSSSSSSSS")
        activityViewModel.user.collect { acc ->
            if (acc != null) {
                binding.listSubjects.isVisible = true
            } else {
                binding.listSubjects.isVisible = false
                performSignIn()
            }
        }
        timetable
            .catch { println(it) }
            .collect { state ->
                when (state) {
                    is State.Error -> println(state.throwable)
                    is State.Loading -> showProgress(true)
                    is State.Success -> {
                        showProgress(false)
                        timetableAdapter.submitList(state.data?.toMutableList())
                        println("Data: ${state.data}")
                    }
                }
            }
        refresh()
    }

    private fun showProgress(isVisible: Boolean = true) {
        with(binding) {
            progressBar.isVisible = isVisible
            listSubjects.isVisible = !isVisible
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun performSignIn() {
        loginResultHandler.launch(
            IntentSenderRequest.Builder(
                PendingIntent.getActivity(
                    requireContext(),
                    GOOGLE_SIGN_IN_REQUEST_CODE,
                    googleSignInClient.signInIntent,
                    PendingIntent.FLAG_ONE_SHOT
                )
            ).build()
        )
    }

    override fun onListEmpty() {
    }

    override fun onListHasData() {
    }
}

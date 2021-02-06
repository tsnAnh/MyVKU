package dev.tsnanh.myvku.base

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Job

abstract class BaseActivity : AppCompatActivity() {
    abstract val viewModel: BaseViewModel

    protected var jobs = mutableListOf<Job>()

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}
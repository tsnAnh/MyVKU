package dev.tsnanh.myvku.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job

abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {
    protected var jobs = mutableListOf<Job>()
    protected abstract val viewModel: VM

    protected lateinit var binding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = initDataBinding()
        binding.initViews()
        jobs.add(
            lifecycleScope.launchWhenStarted {
                viewModel.observeData()
            }
        )
    }

    protected abstract fun initDataBinding(): DB

    protected abstract fun DB.initViews()

    protected abstract fun VM.observeData()

    override fun onDestroy() {
        super.onDestroy()
        jobs.forEach(Job::cancel)
    }
}

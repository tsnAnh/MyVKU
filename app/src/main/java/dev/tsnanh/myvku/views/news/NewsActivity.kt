/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.news

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.base.BaseActivity
import dev.tsnanh.myvku.databinding.ActivityNewsBinding
import dev.tsnanh.myvku.utils.Constants
import dev.tsnanh.myvku.views.main.MainViewModel

/**
 * An Activity with webview for display news when user's device doesn't have browser
 */
class NewsActivity : BaseActivity() {
    private lateinit var binding: ActivityNewsBinding
    override val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)

        val navArgs: NewsActivityArgs by navArgs()

        // load content
        binding.webView.apply {
            loadUrl(Constants.DAO_TAO_URL + navArgs.url)
            setTitle(navArgs.title)
        }
    }

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }
}

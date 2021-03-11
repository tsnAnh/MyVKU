/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.myvku.views.news

import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.navArgs
import dev.tsnanh.myvku.R
import dev.tsnanh.myvku.base.BaseActivity
import dev.tsnanh.myvku.databinding.ActivityNewsBinding
import dev.tsnanh.myvku.utils.Constants

/**
 * An Activity with webview for display news when user's device doesn't have browser
 */
class NewsActivity : BaseActivity<NewsViewModel, ActivityNewsBinding>() {
    override val viewModel: NewsViewModel by viewModels()

    override fun onNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun initDataBinding(): ActivityNewsBinding =
        DataBindingUtil.setContentView(this, R.layout.activity_news)

    override fun ActivityNewsBinding.initViews() {
        val navArgs: NewsActivityArgs by navArgs()
        webView.apply {
            loadUrl(Constants.DAO_TAO_URL + navArgs.url)
            setTitle(navArgs.title)
        }
    }

    override fun NewsViewModel.observeData() {
    }
}

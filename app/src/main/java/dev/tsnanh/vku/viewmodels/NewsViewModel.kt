/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.entities.Resource
import dev.tsnanh.vku.domain.usecases.RetrieveNewsUseCase
import dev.tsnanh.vku.utils.SecretConstants
import org.koin.java.KoinJavaComponent.inject

class NewsViewModel : ViewModel() {
    private val retrieveNewsUseCase by inject(RetrieveNewsUseCase::class.java)

    private var _news = retrieveNewsUseCase.execute(SecretConstants.NEWS_URL, "0")
    val news: LiveData<Resource<List<News?>>>
        get() = _news
}

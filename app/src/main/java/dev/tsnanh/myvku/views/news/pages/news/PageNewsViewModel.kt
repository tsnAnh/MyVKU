package dev.tsnanh.myvku.views.news.pages.news

import androidx.hilt.lifecycle.ViewModelInject
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveNewsUseCase

class PageNewsViewModel @ViewModelInject constructor(
    retrieveNewsUseCase: RetrieveNewsUseCase,
) : BaseViewModel() {
    val news = retrieveNewsUseCase.execute()
}
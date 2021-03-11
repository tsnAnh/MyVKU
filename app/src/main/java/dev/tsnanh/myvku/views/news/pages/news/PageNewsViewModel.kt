package dev.tsnanh.myvku.views.news.pages.news

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveNewsUseCase
import javax.inject.Inject

@HiltViewModel
class PageNewsViewModel @Inject constructor(
    retrieveNewsUseCase: RetrieveNewsUseCase,
) : BaseViewModel() {
    val news = retrieveNewsUseCase.execute()
}

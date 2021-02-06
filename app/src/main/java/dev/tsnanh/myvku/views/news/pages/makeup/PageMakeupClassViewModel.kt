package dev.tsnanh.myvku.views.news.pages.makeup

import androidx.hilt.lifecycle.ViewModelInject
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveNoticeUseCase

class PageMakeupClassViewModel @ViewModelInject constructor(
    retrieveNoticeUseCase: RetrieveNoticeUseCase,
) : BaseViewModel() {
    val makeUpClass = retrieveNoticeUseCase.makeUpClass("")
}
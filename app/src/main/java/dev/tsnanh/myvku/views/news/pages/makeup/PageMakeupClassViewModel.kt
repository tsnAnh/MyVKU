package dev.tsnanh.myvku.views.news.pages.makeup

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveNoticeUseCase
import javax.inject.Inject

@HiltViewModel
class PageMakeupClassViewModel @Inject constructor(
    retrieveNoticeUseCase: RetrieveNoticeUseCase,
) : BaseViewModel() {
    val makeUpClass = retrieveNoticeUseCase.makeUpClass("")
}

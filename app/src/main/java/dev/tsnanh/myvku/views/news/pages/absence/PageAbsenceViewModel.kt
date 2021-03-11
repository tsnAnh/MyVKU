package dev.tsnanh.myvku.views.news.pages.absence

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveNoticeUseCase
import javax.inject.Inject

@HiltViewModel
class PageAbsenceViewModel @Inject constructor(
    retrieveNoticeUseCase: RetrieveNoticeUseCase,
) : BaseViewModel() {
    val absences = retrieveNoticeUseCase.absence("")
}

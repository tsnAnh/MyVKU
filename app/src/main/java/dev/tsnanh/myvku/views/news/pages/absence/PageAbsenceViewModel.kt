package dev.tsnanh.myvku.views.news.pages.absence

import androidx.hilt.lifecycle.ViewModelInject
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveNoticeUseCase

class PageAbsenceViewModel @ViewModelInject constructor(
    retrieveNoticeUseCase: RetrieveNoticeUseCase,
) : BaseViewModel() {
    val absences = retrieveNoticeUseCase.absence("")
}
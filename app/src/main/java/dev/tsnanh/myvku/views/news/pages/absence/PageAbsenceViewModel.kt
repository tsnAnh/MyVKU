package dev.tsnanh.myvku.views.news.pages.absence

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveAbsencesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageAbsenceViewModel @Inject constructor(
    retrieveNoticeUseCase: RetrieveAbsencesUseCase,
) : BaseViewModel() {
    val absences = retrieveNoticeUseCase.getAbsences()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                retrieveNoticeUseCase.refresh()
            } catch (e: Exception) {

            }
        }
    }
}

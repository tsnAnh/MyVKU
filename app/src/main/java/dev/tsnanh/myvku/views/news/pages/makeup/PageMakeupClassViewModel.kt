package dev.tsnanh.myvku.views.news.pages.makeup

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveMakeupClassesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageMakeupClassViewModel @Inject constructor(
    retrieveNoticeUseCase: RetrieveMakeupClassesUseCase,
) : BaseViewModel() {
    val makeUpClass = retrieveNoticeUseCase.getMakeupClasses()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                retrieveNoticeUseCase.refresh()
            } catch (e: Exception) {

            }
        }
    }
}

package dev.tsnanh.myvku.views.news.pages.news

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.tsnanh.myvku.base.BaseViewModel
import dev.tsnanh.myvku.domain.usecases.RetrieveNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageNewsViewModel @Inject constructor(
    retrieveNewsUseCase: RetrieveNewsUseCase,
) : BaseViewModel() {
    val news = retrieveNewsUseCase.execute()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                retrieveNewsUseCase.refresh()
            } catch (e: Exception) {

            }
        }
    }
}

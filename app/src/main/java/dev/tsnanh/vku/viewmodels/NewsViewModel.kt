/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.Chip
import dev.tsnanh.vku.domain.entities.News
import dev.tsnanh.vku.domain.usecases.RetrieveNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class NewsViewModel : ViewModel() {

    private val retrieveNewsUseCase by inject(RetrieveNewsUseCase::class.java)

    val news = retrieveNewsUseCase.execute()
    var listNewsLocal: List<News>? = null

    private val _navigateToView = MutableLiveData<News>()
    val navigateToView: LiveData<News>
        get() = _navigateToView

    private val _shareAction = MutableLiveData<News>()
    val shareAction: LiveData<News>
        get() = _shareAction

    private val _filterData = MutableLiveData<String>()
    val filterData: LiveData<String>
        get() = _filterData

    fun refreshNews() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                retrieveNewsUseCase.refresh()
            }
        }
    }

    fun onNavigateToView(news: News) {
        _navigateToView.value = news
    }

    fun onNavigatedToView() {
        _navigateToView.value = null
    }

    fun onShareButtonClick(news: News) {
        _shareAction.value = news
    }

    fun onShareButtonClicked() {
        _shareAction.value = null
    }

    fun onChipClick(category: View) {
        val chip = category as Chip
        if (chip.isChecked) {
            listNewsLocal = news.value
            _filterData.value = chip.tag as String
        } else {
            _filterData.value = null
            viewModelScope.launch { retrieveNewsUseCase.refresh() }
            listNewsLocal = news.value
        }
    }
}

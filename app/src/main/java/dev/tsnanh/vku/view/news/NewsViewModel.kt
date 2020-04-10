package dev.tsnanh.vku.view.news

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.Chip
import dev.tsnanh.vku.domain.News
import dev.tsnanh.vku.repository.VKURepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject

class NewsViewModel : ViewModel() {

    private val repository: VKURepository by inject(VKURepository::class.java)

    val listNews = repository.news

    private val _navigateToView = MutableLiveData<News>()
    val navigateToView: LiveData<News>
        get() = _navigateToView

    private val _shareAction = MutableLiveData<News>()
    val shareAction: LiveData<News>
        get() = _shareAction

    private val _isFiltered = MutableLiveData<Boolean>()
    val isFiltered: LiveData<Boolean>
        get() = _isFiltered

    private val _filterData = MutableLiveData<String>()
    val filterData: LiveData<String>
        get() = _filterData

    private val _refreshNews = MutableLiveData(false)
    val refreshNews: LiveData<Boolean>
        get() = _refreshNews

    private fun onNewsRefresh() {
        _refreshNews.value = true
    }

    private fun onNewsRefreshed() {
        _refreshNews.value = false
    }

    init {
        viewModelScope.launch {
            refresh()
        }
    }

    fun refreshNews() {
        viewModelScope.launch {
            onNewsRefresh()
            refresh()
            onNewsRefreshed()
        }
    }

    private suspend fun refresh() {
        withContext(Dispatchers.IO) {
            repository.refreshNews()
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
            // Switch to filter adapter
            _isFiltered.value = true
            _filterData.value = chip.tag.toString()
        } else {
            _isFiltered.value = false
        }
    }

    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }
}

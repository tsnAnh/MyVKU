/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels.my_vku

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.chip.Chip
import dev.tsnanh.vku.R
import dev.tsnanh.vku.domain.entities.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketException
import java.net.SocketTimeoutException

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    // private val retrieveNewsUseCase by inject(RetrieveNewsUseCase::class.java)

    // val news = retrieveNewsUseCase.execute()
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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    fun onErrorDisplayed() {
        _error.value = null
    }

    fun refreshNews() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    // retrieveNewsUseCase.refresh()
                } catch (ste: SocketTimeoutException) {
                    _error.postValue(getApplication<Application>().getString(R.string.err_msg_request_timeout))
                } catch (ske: SocketException) {
                    _error.postValue(getApplication<Application>().getString(R.string.err_msg_socket_exception))
                } catch (e: Exception) {
                    _error.postValue(getApplication<Application>().getString(R.string.err_msg_something_went_wrong))
                }
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
            // listNewsLocal = news.value
            _filterData.value = chip.tag as String
        } else {
            _filterData.value = null
            refreshNews()
            // listNewsLocal = news.value
        }
    }
}

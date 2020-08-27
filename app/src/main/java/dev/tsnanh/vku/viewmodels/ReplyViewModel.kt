/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.domain.usecases.RetrievePageCountOfThreadUseCase

class RepliesViewModel @ViewModelInject constructor(
    private val retrievePageCountOfThreadUseCase: RetrievePageCountOfThreadUseCase
) : ViewModel() {
    fun pageCount(threadId: String) = retrievePageCountOfThreadUseCase.invoke(threadId, 10)
}
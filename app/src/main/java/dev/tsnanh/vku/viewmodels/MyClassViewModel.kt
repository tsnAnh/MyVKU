/*
 * Copyright (c) 2020 My VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.repository.VKURepository
import org.koin.java.KoinJavaComponent.inject

class MyClassViewModel : ViewModel() {
    private val repository by inject(VKURepository::class.java)
}

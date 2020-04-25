/*
 * Copyright (c) 2020 VKU by tsnAnh
 */

package dev.tsnanh.vku.viewmodels

import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.repository.VKURepository
import org.koin.java.KoinJavaComponent.inject

class RelaxViewModel : ViewModel() {
    private val repository by inject(VKURepository::class.java)
}

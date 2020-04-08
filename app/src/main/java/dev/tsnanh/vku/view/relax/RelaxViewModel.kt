package dev.tsnanh.vku.view.relax

import androidx.lifecycle.ViewModel
import dev.tsnanh.vku.repository.VKURepository
import org.koin.java.KoinJavaComponent.inject

class RelaxViewModel : ViewModel() {
    private val repository by inject(VKURepository::class.java)
}

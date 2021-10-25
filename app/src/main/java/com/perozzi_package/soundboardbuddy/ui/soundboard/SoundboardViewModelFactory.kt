package com.perozzi_package.soundboardbuddy.ui.soundboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.perozzi_package.soundboardbuddy.data.repositories.SoundRepository

class SoundViewModelFactory(
    private val repository: SoundRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SoundViewModel(repository) as T
    }
}
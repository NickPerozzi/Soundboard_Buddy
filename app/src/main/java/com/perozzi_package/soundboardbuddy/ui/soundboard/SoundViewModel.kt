package com.perozzi_package.soundboardbuddy.ui.soundboard

import androidx.lifecycle.ViewModel
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundGridItem
import com.perozzi_package.soundboardbuddy.data.repositories.SoundRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SoundViewModel (
    private val repository: SoundRepository
): ViewModel() {

    fun upsert(item: SoundGridItem) = CoroutineScope(Dispatchers.Main).launch {
        repository.upsert(item)
    }

    fun delete(item: SoundGridItem) = CoroutineScope(Dispatchers.Main).launch {
        repository.delete(item)
    }

    fun getAllSoundItems() = repository.getAllSoundItems()
}
package com.perozzi_package.soundboardbuddy.ui.soundboard

import com.perozzi_package.soundboardbuddy.data.db.entities.SoundboardItem

interface AddDialogListener {
    fun onAddButtonClicked(item: SoundboardItem)
}
package com.perozzi_package.soundboardbuddy.ui.soundboard

import com.perozzi_package.soundboardbuddy.data.db.entities.SoundGridItem

interface AddDialogListener {
    fun onAddButtonClicked(item: SoundGridItem)
}
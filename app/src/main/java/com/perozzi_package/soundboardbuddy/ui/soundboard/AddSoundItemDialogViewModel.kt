package com.perozzi_package.soundboardbuddy.ui.soundboard

import android.app.Application
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class AddSoundItemDialogViewModel() : ViewModel() {

    var recordSelectedNotUpload: MutableLiveData<Boolean> = MutableLiveData(false)

    var recordAudioText: MutableLiveData<String> = MutableLiveData("Nice!")

    var uploadButtonVisibility = recordSelectedNotUpload.map { recordSelectedNotUpload ->
        if (recordSelectedNotUpload) {
            View.GONE
        } else {
            View.VISIBLE
        }
    } as MutableLiveData<Int>

    var recordButtonVisibility = recordSelectedNotUpload.map { recordSelectedNotUpload ->
        if (recordSelectedNotUpload) {
            View.VISIBLE
        } else {
            View.GONE
        }
    } as MutableLiveData<Int>
}
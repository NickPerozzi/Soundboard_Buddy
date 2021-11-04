package com.perozzi_package.soundboardbuddy.ui.soundboard

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap

class AddItemDialogViewModel() : ViewModel() {

    var recordSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    var uploadSelected: MutableLiveData<Boolean> = MutableLiveData(false)
    var presetSelected: MutableLiveData<Boolean> = MutableLiveData(false)

    var recordAudioText: MutableLiveData<String> = MutableLiveData("Nice!")

    var uploadButtonVisibility = uploadSelected.map { uploadSelected ->
        if (uploadSelected) {
            View.VISIBLE
        } else {
            View.GONE
        }
    } as MutableLiveData<Int>

    var recordButtonVisibility = recordSelected.map { recordSelected ->
        if (recordSelected) {
            View.VISIBLE
        } else {
            View.GONE
        }
    } as MutableLiveData<Int>

    var presetSpinnerVisibility = presetSelected.map { presetSelected ->
        if (presetSelected) {
            View.VISIBLE
        } else {
            View.GONE
        }
    } as MutableLiveData<Int>


}
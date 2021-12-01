package com.perozzi_package.soundboardbuddy.ui.soundboard

import android.media.MediaRecorder
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import soundboardbuddy.R

class AddItemDialogViewModel() : ViewModel() {

    var typeOfAudioSelected: MutableLiveData<String> = MutableLiveData("")

    var recordSelected: MutableLiveData<Boolean> = typeOfAudioSelected.map { type ->
        type == "Record" } as MutableLiveData<Boolean>
    var uploadSelected: MutableLiveData<Boolean> = typeOfAudioSelected.map { type ->
        type == "Upload" } as MutableLiveData<Boolean>
    var presetSelected: MutableLiveData<Boolean> = typeOfAudioSelected.map { type ->
        type == "Preset" } as MutableLiveData<Boolean>

    var recordAudioText: MutableLiveData<String> = MutableLiveData("Nice!")
    var uploadedFileName: MutableLiveData<String> = MutableLiveData("")

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

    // I could pass in resources to my ViewModel and do something like
    // = resources.getStringArray(R.array.preset_spinner_options)
    // but I am reluctant to pass resources into ViewModel
    var presetSpinnerOptions= listOf(
        "Airhorn",
        "Among us report",
        "Among us start",
        "Bruh",
        "Discord enter",
        "Discord exit",
        "Discord ping",
        "Doge bonk",
        "Explosion",
        "Hitmarker",
        "iCarly laugh track",
        "Inception drop",
        "Knocking",
        "Oof",
        "Snap, noice",
        "Taco Bell bong",
        "Vine boom",
        "We'll be right back",
        "What the dog doin?",
        "Windows error",
        "Windows shutdown",
        "Windows startup"
    )


    var selectedPresetAudioIndex: MutableLiveData<Int> = MutableLiveData(0)
    var selectedPresetAudio = selectedPresetAudioIndex.map { index ->
        listOfPresetDrawables[index]
    }

    var listOfPresetDrawables = listOf(
        R.raw.airhorn,
        R.raw.among_us_report,
        R.raw.among_us_start,
        R.raw.bruh,
        R.raw.discord_enter,
        R.raw.discord_exit,
        R.raw.discord_ping,
        R.raw.doge_bonk,
        R.raw.explosion,
        R.raw.hitmark,
        R.raw.icarly_laugh_track,
        R.raw.inception_drop,
        R.raw.knocking,
        R.raw.oof,
        R.raw.snap_noice,
        R.raw.taco_bell,
        R.raw.vine_boom,
        R.raw.well_be_right_back,
        R.raw.what_da_dog_doin,
        R.raw.windows_error,
        R.raw.windows_shutdown,
        R.raw.windows_startup
    )

    fun setupMediaRecorder(recordingPath: String) {
    }

    var promptVisibility = typeOfAudioSelected.map { type ->
        if (type != "") {
            View.VISIBLE
        } else {
            View.GONE
        }
    } as MutableLiveData<Int>

    var audioPromptText = typeOfAudioSelected.map { type ->
        when (type) {
            "Upload" -> {
                "Upload your audio:"
            }
            "Record" -> {
                "Record your audio:"
            }
            "Preset" -> {
                "Select your audio:"
            }
            else -> {
                ""
            }
        }
    } as MutableLiveData<String>

    var buttonColor: MutableLiveData<Int> = uploadSelected.switchMap { uploadSelected ->
        recordSelected.switchMap { recordSelected ->
            presetSelected.map { presetSelected ->
                when {
                    uploadSelected -> {
                        R.drawable.play_button_blue
                    }
                    recordSelected -> {
                        R.drawable.play_button_red
                    }
                    presetSelected -> {
                        R.drawable.play_button_white
                    }
                    else -> {
                        R.drawable.play_button_black
                    }
                }
            }
        }
    } as MutableLiveData<Int>


}
package com.perozzi_package.soundboardbuddy.ui.soundboard

import androidx.lifecycle.ViewModel
/*
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundboardItem
*/

class SoundboardViewModel() : ViewModel() {

/*    fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if
    }*/

/*    private val soundNamesList: MutableLiveData<MutableList<String>> =
        MutableLiveData(mutableListOf())
    private val soundSoundsList: MutableLiveData<MutableList<MediaPlayer>> =
        MutableLiveData(mutableListOf())
    private val buttonColorsList: MutableLiveData<MutableList<Int>> =
        MutableLiveData(mutableListOf())

    val arrayForSoundboard = soundNamesList.switchMap { soundNamesList ->
        soundSoundsList.switchMap { soundSoundsList ->
            buttonColorsList.map { buttonColorsList ->
                setDataInSoundboard(soundNamesList, soundSoundsList, buttonColorsList)
            }
        }
    }

    private fun setDataInSoundboard(
        soundNames: MutableList<String>,
        sounds: MutableList<MediaPlayer>,
        buttonColors: MutableList<Int>
    ): ArrayList<SoundboardItem> {
        val items: ArrayList<SoundboardItem> = ArrayList()
        for (i in 0 until soundNames.size) {
            items.add(
                SoundboardItem(
                    soundNames[i],
                    sounds[i],
                    buttonColors[i]
                )
            )
        }
        return items
    }*/


}
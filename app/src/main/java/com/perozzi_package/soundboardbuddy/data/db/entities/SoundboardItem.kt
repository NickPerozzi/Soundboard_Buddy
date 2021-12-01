package com.perozzi_package.soundboardbuddy.data.db.entities

import android.media.MediaPlayer
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soundboard")
data class SoundboardItem(
    @ColumnInfo(name = "sound_name")
    var name: String,
    @ColumnInfo(name = "sound_file")
    var audioPath: String,
    @ColumnInfo(name = "button_color")
    var color: Int
) {
    @PrimaryKey(autoGenerate = true) // necessary so that individual values are unique
    var id: Int? = null
}
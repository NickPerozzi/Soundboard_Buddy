package com.perozzi_package.soundboardbuddy.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "soundboard")
data class SoundGridItem(
    @ColumnInfo(name = "sound_name")
    var name: String,
    @ColumnInfo(name = "sound_file")
    var mp3: Int?,
    //@ColumnInfo(name = "button_color")
    //var color: Int
) {
    @PrimaryKey(autoGenerate = true) // necessary so that individual values are unique
    var id: Int? = null
}
package com.perozzi_package.soundboardbuddy.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundGridItem

@Database(
    entities = (arrayOf(SoundGridItem::class)),
    version = 1
)

abstract class SoundDatabase: RoomDatabase() {
    abstract fun getItemDao(): SoundDao

    companion object {

        @Volatile
        private var instance: SoundDatabase? = null
        private val LOCK = Any()

        // executed whenever we create an instance of sound db class
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                SoundDatabase::class.java,"SoundDB.db").build()
    }

}
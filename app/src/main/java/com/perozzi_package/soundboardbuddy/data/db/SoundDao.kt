package com.perozzi_package.soundboardbuddy.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundboardItem

@Dao
interface SoundDao {

    // one place for repository to get ***information*** from (i.e. local database)
    // dao equivalent for api's is called a service layer

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: SoundboardItem) // insert, if it exists already it will update it

    @Delete
    suspend fun delete(item: SoundboardItem)

    @Query("SELECT * FROM soundboard")
    fun getAllSoundItems(): LiveData<List<SoundboardItem>>
}
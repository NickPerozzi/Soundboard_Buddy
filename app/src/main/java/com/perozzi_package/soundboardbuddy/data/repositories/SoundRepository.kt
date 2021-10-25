package com.perozzi_package.soundboardbuddy.data.repositories

import com.perozzi_package.soundboardbuddy.data.db.SoundDatabase
import com.perozzi_package.soundboardbuddy.data.db.entities.SoundGridItem

// repository: the place where logic that determines where to get data (online/offline, returning
// user vs new user, a drug dealer may or may not be a good analogy for a repository
// waiter

class SoundRepository(
    private val db: SoundDatabase
) {
    suspend fun upsert(item: SoundGridItem) = db.getItemDao().upsert(item)

    suspend fun delete(item: SoundGridItem) = db.getItemDao().delete(item)

    fun getAllSoundItems() = db.getItemDao().getAllSoundItems()
}
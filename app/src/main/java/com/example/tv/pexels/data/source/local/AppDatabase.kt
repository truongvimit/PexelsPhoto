package com.example.tv.pexels.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tv.pexels.data.model.room.SearchQueryEntity
import com.example.tv.pexels.data.model.room.PhotoEntity

@Database(
    entities = [SearchQueryEntity::class, PhotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchQueryDao(): SearchQueryDao
    abstract fun photoDao(): PhotoDao
}

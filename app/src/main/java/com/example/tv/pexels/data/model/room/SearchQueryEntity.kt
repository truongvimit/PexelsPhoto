package com.example.tv.pexels.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_query_table")
data class SearchQueryEntity(
    @PrimaryKey
    val searchQuery: String,
    val createdAt: Long = System.currentTimeMillis()
)
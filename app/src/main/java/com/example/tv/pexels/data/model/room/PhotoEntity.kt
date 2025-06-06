package com.example.tv.pexels.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_table")
data class PhotoEntity(
    @PrimaryKey
    val id: Long,
    val width: Long? = null,
    val height: Long? = null,
    val url: String? = null,
    val photographer: String? = null,
    val photographerUrl: String? = null,
    val photographerId: Long? = null,
    val avgColor: String? = null,
    val srcOriginal: String? = null,
    val srcMedium: String? = null,
    val srcSmall: String? = null,
    val alt: String? = null,
    val isLiked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
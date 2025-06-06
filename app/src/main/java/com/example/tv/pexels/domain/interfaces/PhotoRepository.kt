package com.example.tv.pexels.domain.interfaces

import com.example.tv.pexels.data.model.room.PhotoEntity
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun getAllLikedPhotos(): Flow<MutableList<PhotoEntity>>
    suspend fun getAllDislikedPhotos(): Flow<MutableList<PhotoEntity>>
    suspend fun addPhoto(photoEntity: PhotoEntity)
    suspend fun deletePhotoById(id: Long)
    suspend fun getPhotoById(id: Long): PhotoEntity?
    suspend fun getAllPhotos(): Flow<MutableList<PhotoEntity>>
}

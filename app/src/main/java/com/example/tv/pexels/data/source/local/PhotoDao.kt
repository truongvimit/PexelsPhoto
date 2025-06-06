package com.example.tv.pexels.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tv.pexels.core.Constants
import com.example.tv.pexels.data.model.room.PhotoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {
    @Query(Constants.Queries.GET_ALL_LIKED_PHOTOS)
    fun getAllLikedPhotos(): Flow<MutableList<PhotoEntity>>

    @Query(Constants.Queries.GET_ALL_DISLIKED_PHOTOS)
    fun getAllDislikedPhotos(): Flow<MutableList<PhotoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPhoto(photoEntity: PhotoEntity)

    @Query(Constants.Queries.DELETE_PHOTO_BY_ID)
    suspend fun deletePhotoById(id: Long)

    @Query(Constants.Queries.GET_PHOTO_BY_ID)
    suspend fun getPhotoById(id: Long): PhotoEntity?

    @Query(Constants.Queries.GET_ALL_PHOTOS)
    fun getAllPhotos(): Flow<MutableList<PhotoEntity>>
}

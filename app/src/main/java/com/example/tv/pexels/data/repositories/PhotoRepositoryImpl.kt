package com.example.tv.pexels.data.repositories

import com.example.tv.pexels.data.model.room.PhotoEntity
import com.example.tv.pexels.data.source.local.PhotoDao
import com.example.tv.pexels.domain.interfaces.PhotoRepository
import kotlinx.coroutines.flow.Flow

class PhotoRepositoryImpl(
    private val photoDao: PhotoDao
) : PhotoRepository {
    override suspend fun getAllLikedPhotos(): Flow<MutableList<PhotoEntity>> =
        photoDao.getAllLikedPhotos()

    override suspend fun getAllDislikedPhotos(): Flow<MutableList<PhotoEntity>> =
        photoDao.getAllDislikedPhotos()

    override suspend fun addPhoto(photoEntity: PhotoEntity) = photoDao.addPhoto(photoEntity)

    override suspend fun deletePhotoById(id: Long) = photoDao.deletePhotoById(id)

    override suspend fun getPhotoById(id: Long): PhotoEntity? = photoDao.getPhotoById(id)

    override suspend fun getAllPhotos(): Flow<MutableList<PhotoEntity>> =
        photoDao.getAllPhotos()
}

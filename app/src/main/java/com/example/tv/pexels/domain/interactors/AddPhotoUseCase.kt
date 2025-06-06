package com.example.tv.pexels.domain.interactors

import com.example.tv.pexels.data.model.room.PhotoEntity
import com.example.tv.pexels.domain.interfaces.PhotoRepository

class AddPhotoUseCase(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(photoEntity: PhotoEntity) {
        photoRepository.addPhoto(photoEntity)
    }
}
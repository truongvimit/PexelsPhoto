package com.example.tv.pexels.domain.interactors

import com.example.tv.pexels.domain.interfaces.PhotoRepository

class GetAllDislikedPhotosUseCase(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke() = photoRepository.getAllDislikedPhotos()
}
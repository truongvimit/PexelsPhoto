package com.example.tv.pexels.data.model.room

import com.example.tv.pexels.data.model.response.Photo

fun Photo.toPhotoEntity(isLiked: Boolean): PhotoEntity {
    return PhotoEntity(
        id = this.id ?: 0L,
        width = this.width,
        height = this.height,
        url = this.url,
        photographer = this.photographer,
        photographerUrl = this.photographerUrl,
        photographerId = this.photographerId,
        avgColor = this.avgColor,
        srcOriginal = this.src?.original,
        srcMedium = this.src?.medium,
        srcSmall = this.src?.small,
        alt = this.alt,
        isLiked = isLiked
    )
}
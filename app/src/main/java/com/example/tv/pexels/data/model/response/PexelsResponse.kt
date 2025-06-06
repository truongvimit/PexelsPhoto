package com.example.tv.pexels.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PexelsResponse (
    val page: Long? = null,

    @SerialName("per_page")
    val perPage: Long? = null,

    val photos: List<Photo>? = null,

    @SerialName("total_results")
    val totalResults: Long? = null,

    @SerialName("next_page")
    val nextPage: String? = null
)

@Serializable
data class Photo (
    val id: Long? = null,
    val width: Long? = null,
    val height: Long? = null,
    val url: String? = null,
    val photographer: String? = null,

    @SerialName("photographer_url")
    val photographerUrl: String? = null,

    @SerialName("photographer_id")
    val photographerId: Long? = null,

    @SerialName("avg_color")
    val avgColor: String? = null,

    val src: Src? = null,
    val liked: Boolean? = null,
    val alt: String? = null
)

@Serializable
data class Src (
    val original: String? = null,

    @SerialName("large2x")
    val large2X: String? = null,

    val large: String? = null,
    val medium: String? = null,
    val small: String? = null,
    val portrait: String? = null,
    val landscape: String? = null,
    val tiny: String? = null
)

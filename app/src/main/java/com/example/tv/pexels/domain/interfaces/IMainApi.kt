package com.example.tv.pexels.domain.interfaces

import com.example.tv.pexels.core.Constants
import com.example.tv.pexels.data.model.response.PexelsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface IMainApi {
    @GET("v1/curated")
    suspend fun getCuratedPhotos(
        @Header("Authorization") apiKey: String = Constants.MainApi.API_KEY,
        @Query("per_page") perPage: Int = 1,
        @Query("page") page: Int = 1
    ): Response<PexelsResponse>

    @GET("v1/search")
    suspend fun searchPhotos(
        @Header("Authorization") apiKey: String = Constants.MainApi.API_KEY,
        @Query("query") query: String,
        @Query("per_page") perPage: Int = 1,
        @Query("page") page: Int = 1
    ): Response<PexelsResponse>
}

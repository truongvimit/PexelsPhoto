package com.example.tv.pexels.domain.interfaces

import com.example.tv.pexels.data.model.room.SearchQueryEntity
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getAllSearchQueries(): Flow<MutableList<SearchQueryEntity>>
    suspend fun addSearchQuery(searchQueryEntity: SearchQueryEntity)
    suspend fun deleteSearchQueryById(searchQuery: String)
}

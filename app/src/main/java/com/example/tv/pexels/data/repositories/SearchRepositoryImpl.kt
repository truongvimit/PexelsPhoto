package com.example.tv.pexels.data.repositories

import com.example.tv.pexels.data.model.room.SearchQueryEntity
import com.example.tv.pexels.data.source.local.SearchQueryDao
import com.example.tv.pexels.domain.interfaces.SearchRepository
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val searchQueryDao: SearchQueryDao
): SearchRepository {
    override suspend fun getAllSearchQueries(): Flow<MutableList<SearchQueryEntity>> = searchQueryDao.getAllSearchQueries()

    override suspend fun addSearchQuery(searchQueryEntity: SearchQueryEntity) = searchQueryDao.addSearchQuery(searchQueryEntity)
    override suspend fun deleteSearchQueryById(searchQuery: String) =
        searchQueryDao.deleteSearchQueryById(searchQuery)
}

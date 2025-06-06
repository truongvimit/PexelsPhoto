package com.example.tv.pexels.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tv.pexels.core.Constants
import com.example.tv.pexels.data.model.room.SearchQueryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchQueryDao {
    @Query(Constants.Queries.GET_ALL_SEARCH_QUERIES)
    fun getAllSearchQueries(): Flow<MutableList<SearchQueryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSearchQuery(searchQueryEntity: SearchQueryEntity)

    @Query(Constants.Queries.DELETE_SEARCH_QUERY_BY_ID)
    suspend fun deleteSearchQueryById(searchQuery: String)

}

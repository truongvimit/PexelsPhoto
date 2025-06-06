package com.example.tv.pexels.domain.interactors

import com.example.tv.pexels.data.model.room.SearchQueryEntity
import com.example.tv.pexels.domain.interfaces.SearchRepository


class AddSearchQueryUseCase(
    private val searchRepository: SearchRepository,
) {
    suspend operator fun invoke(searchQueryEntity: SearchQueryEntity) =
        searchRepository.addSearchQuery(searchQueryEntity)
}
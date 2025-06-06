package com.example.tv.pexels.domain.interactors

import com.example.tv.pexels.domain.interfaces.SearchRepository

class DeleteSearchQueryByQueryUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(searchQuery: String) =
        searchRepository.deleteSearchQueryById(searchQuery)
}
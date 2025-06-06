package com.example.tv.pexels.domain.interactors

import com.example.tv.pexels.domain.interfaces.SearchRepository

class GetAllSearchUseCase(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke() = searchRepository.getAllSearchQueries()
}
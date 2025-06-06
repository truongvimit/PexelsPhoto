package com.example.tv.pexels.presentation.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.tv.pexels.data.model.response.Photo
import com.example.tv.pexels.data.model.room.SearchQueryEntity
import com.example.tv.pexels.data.remote.SearchPhotoPagingSource
import com.example.tv.pexels.domain.interactors.AddSearchQueryUseCase
import com.example.tv.pexels.domain.interactors.DeleteSearchQueryByQueryUseCase
import com.example.tv.pexels.domain.interactors.GetAllSearchUseCase
import com.example.tv.pexels.domain.interfaces.IMainApi
import com.example.tv.pexels.presentation.base.BaseViewModel
import com.example.tv.pexels.presentation.ui.activity.NotificationViewModel
import com.example.tv.pexels.presentation.ui.components.LoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    savedStateHandle: SavedStateHandle,
    private val notificationViewModel: NotificationViewModel,
    private val iMainApi: IMainApi,
    private val addSearchQueryUseCase: AddSearchQueryUseCase,
    private val deleteSearchQueryByQueryUseCase: DeleteSearchQueryByQueryUseCase,
    private val getAllSearchUseCase: GetAllSearchUseCase,
) : BaseViewModel() {
    private val _stateFlow: MutableStateFlow<SearchState> = MutableStateFlow(
        SearchState(loadingState = LoadingState.Loading)
    )
    val stateFlow: StateFlow<SearchState> = _stateFlow.asStateFlow()

    override fun onInit() {
        initializeScreen()
    }

    private fun initializeScreen() {
        try {
            // Load recent searches
            loadRecentSearches()

            _stateFlow.update {
                it.copy(loadingState = LoadingState.Success)
            }
        } catch (e: Exception) {
            _stateFlow.update {
                it.copy(
                    loadingState = LoadingState.Error,
                    error = e.message
                )
            }
        }
    }

    fun retry() {
        viewModelScope.launch {
            _stateFlow.update {
                it.copy(loadingState = LoadingState.Loading, error = null)
            }
            initializeScreen()
        }
    }

    private fun loadRecentSearches() {
        viewModelScope.launch {
            getAllSearchUseCase().collect { searchQueries ->
                _stateFlow.update {
                    it.copy(recentSearches = searchQueries)
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _stateFlow.update {
            it.copy(searchQuery = query)
        }
    }

    fun searchPhotos(query: String) {
        if (query.isBlank()) return

        // Add to recent searches
        addToRecentSearches(query.trim())
        _stateFlow.update {
            it.copy(
                isRefreshing = true,
                titleIndicatorLoading = "Searching...",
                searchQuery = query
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            Pager(
                config = PagingConfig(
                    10, enablePlaceholders = true
                )
            ) {
                SearchPhotoPagingSource(
                    iMainApi = iMainApi,
                    query = query,
                    onFirstPageDone = {
                        _stateFlow.update {
                            it.copy(
                                isRefreshing = false,
                            )
                        }
                        viewModelScope.launch {
                            delay(500)
                            _stateFlow.update {
                                it.copy(
                                    titleIndicatorLoading = "Refreshing..."
                                )
                            }
                        }
                    })
            }.flow.cachedIn(viewModelScope).collect { data ->
                _stateFlow.update {
                    it.copy(
                        searchPhotoPagingData = flowOf(data),
                    )
                }
            }
        }
    }

    fun refreshSearch() {
        val currentQuery = _stateFlow.value.searchQuery
        if (currentQuery.isNotEmpty()) {
            _stateFlow.update {
                it.copy(
                    isRefreshing = true,
                    titleIndicatorLoading = "Refreshing..."
                )
            }
            searchPhotos(currentQuery)
        }
    }

    fun clearSearch() {
        _stateFlow.update {
            it.copy(
                searchQuery = "",
                searchPhotoPagingData = null
            )
        }
    }

    private fun addToRecentSearches(query: String) {
        viewModelScope.launch {
            addSearchQueryUseCase(SearchQueryEntity(searchQuery = query))
        }
    }

    fun removeFromRecentSearches(query: String) {
        viewModelScope.launch {
            deleteSearchQueryByQueryUseCase(query)
        }
    }

    fun clearAllRecentSearches() {
        viewModelScope.launch {
            getAllSearchUseCase().collect { entities ->
                entities.forEach { entity ->
                    deleteSearchQueryByQueryUseCase(entity.searchQuery)
                }
            }
        }
    }
}

data class SearchState(
    val loadingState: LoadingState = LoadingState.Loading,
    val error: String? = null,
    val searchPhotoPagingData: Flow<PagingData<Photo>>? = null,
    val isRefreshing: Boolean = false,
    val titleIndicatorLoading: String = "Refreshing...",
    val searchQuery: String = "",
    val recentSearches: List<SearchQueryEntity> = emptyList()
)

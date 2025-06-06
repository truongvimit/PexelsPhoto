package com.example.tv.pexels.presentation.ui.favorite

import androidx.lifecycle.viewModelScope
import com.example.tv.pexels.data.model.room.PhotoEntity
import com.example.tv.pexels.domain.interactors.GetAllLikedPhotosUseCase
import com.example.tv.pexels.presentation.base.BaseViewModel
import com.example.tv.pexels.presentation.ui.components.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val getAllLikedPhotosUseCase: GetAllLikedPhotosUseCase
) : BaseViewModel() {

    private val _stateFlow = MutableStateFlow(FavoriteState())
    val stateFlow: StateFlow<FavoriteState> = _stateFlow.asStateFlow()

    override fun onInit() {
        loadFavoritePhotos()
    }

    private fun loadFavoritePhotos() {
        _stateFlow.update { it.copy(loadingState = LoadingState.Loading) }

        viewModelScope.launch {
            try {
                getAllLikedPhotosUseCase().catch { throwable ->
                    _stateFlow.update {
                        it.copy(
                            loadingState = LoadingState.Error,
                            error = throwable.message
                        )
                    }
                }.collect { photos ->
                    _stateFlow.update {
                        it.copy(
                            loadingState = LoadingState.Success,
                            favoritePhotos = photos,
                            error = null
                        )
                    }
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
    }

    fun retry() {
        loadFavoritePhotos()
    }
}

data class FavoriteState(
    val loadingState: LoadingState = LoadingState.Loading,
    val favoritePhotos: List<PhotoEntity> = emptyList(),
    val error: String? = null
)
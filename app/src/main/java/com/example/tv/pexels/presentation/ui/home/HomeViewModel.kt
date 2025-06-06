package com.example.tv.pexels.presentation.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.tv.pexels.data.model.response.Photo
import com.example.tv.pexels.data.model.room.toPhotoEntity
import com.example.tv.pexels.data.remote.PexelsPhotoPagingSource
import com.example.tv.pexels.domain.interactors.AddPhotoUseCase
import com.example.tv.pexels.domain.interactors.GetAllPhotosUseCase
import com.example.tv.pexels.domain.interfaces.IMainApi
import com.example.tv.pexels.presentation.ui.activity.NotificationViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    savedStateHandle: SavedStateHandle,
    private val notificationViewModel: NotificationViewModel,
    private val iMainApi: IMainApi,
    private val addPhotoUseCase: AddPhotoUseCase,
    private val getAllPhotosUseCase: GetAllPhotosUseCase,
) : ViewModel() {
    private val _stateFlow: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val stateFlow: StateFlow<HomeState> = _stateFlow.asStateFlow()

    private val swipedPhotoIds = mutableSetOf<Long?>()

    init {
        loadPexelsPhotos()
    }

    private fun loadPexelsPhotos() {
        notificationViewModel.isLoading(true)
        viewModelScope.launch(Dispatchers.IO) {
            // Get stored photos once at initialization
            val storedPhotos = getAllPhotosUseCase().first()
            val storedPhotoIds = storedPhotos.map { it.id }.toSet()
            swipedPhotoIds.addAll(storedPhotoIds)

            val pagingFlow = Pager(
                config = PagingConfig(
                    10, enablePlaceholders = true
                )
            ) {
                PexelsPhotoPagingSource(
                    iMainApi = iMainApi,
                    onFirstPageDone = {
                        notificationViewModel.isLoading(false)
                    })
            }.flow
                .cachedIn(viewModelScope)
                .map { pagingData ->
                    pagingData.filter { photo ->
                        photo.id !in swipedPhotoIds
                    }
                }

            _stateFlow.update {
                it.copy(
                    pexelsPhotoPagingData = pagingFlow,
                )
            }
        }
    }

    fun likePhoto(photo: Photo) {
        swipedPhotoIds.add(photo.id)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addPhotoUseCase(photo.toPhotoEntity(isLiked = true))
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun dislikePhoto(photo: Photo) {
        swipedPhotoIds.add(photo.id)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addPhotoUseCase(photo.toPhotoEntity(isLiked = false))
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}

data class HomeState(
    val pexelsPhotoPagingData: Flow<PagingData<Photo>>? = null,
)

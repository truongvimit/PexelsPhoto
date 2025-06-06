package com.example.tv.pexels.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import coil.network.HttpException
import com.example.tv.pexels.data.model.response.Photo
import com.example.tv.pexels.domain.interfaces.IMainApi
import kotlinx.coroutines.delay
import okio.IOException

class PexelsPhotoPagingSource(
    private val iMainApi: IMainApi,
    private val perPage: Int = 20,
    private val onFirstPageDone: () -> Unit = {/* no-op */ }
) : PagingSource<Int, Photo>() {
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 1
        val pexelsResponse = iMainApi.getCuratedPhotos(
            perPage = perPage,
            page = page
        ).body()
        if (page > 1) {
            delay(2000L)
        } else {
            onFirstPageDone()
        }
        return try {
            LoadResult.Page(
                data = pexelsResponse?.photos ?: emptyList(),
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (pexelsResponse?.photos.isNullOrEmpty()) null else page.plus(
                    1
                )
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}
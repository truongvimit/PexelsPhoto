package com.example.tv.pexels.presentation.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.tv.pexels.core.bounceClick
import com.example.tv.pexels.core.clickableNoRipple
import com.example.tv.pexels.data.model.response.Photo
import com.example.tv.pexels.data.model.room.SearchQueryEntity
import com.example.tv.pexels.presentation.ui.components.BaseScreenStateHandler
import com.example.tv.pexels.presentation.ui.components.FullImageView
import com.example.tv.pexels.presentation.ui.components.SurfaceGradient
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(navController: NavHostController) {
    val searchViewModel: SearchViewModel = koinViewModel()
    val uiState by searchViewModel.stateFlow.collectAsStateWithLifecycle()

    BaseScreenStateHandler(
        loadingState = uiState.loadingState,
        onBackClick = { navController.popBackStack() },
        onRetry = { searchViewModel.retry() },
        loadingText = "Loading search...",
        errorTitle = "Failed to load search",
        error = uiState.error
    ) {
        SearchSuccessContent(
            photos = uiState.searchPhotoPagingData?.collectAsLazyPagingItems(),
            isRefreshing = uiState.isRefreshing,
            onRefresh = { searchViewModel.refreshSearch() },
            searchQuery = uiState.searchQuery,
            recentSearches = uiState.recentSearches,
            onSearchQueryChange = { searchViewModel.updateSearchQuery(it) },
            onSearch = { searchViewModel.searchPhotos(it) },
            onRemoveRecentSearch = { searchViewModel.removeFromRecentSearches(it) },
            onClearAllRecentSearches = { searchViewModel.clearAllRecentSearches() },
            onBackClick = { navController.popBackStack() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchSuccessContent(
    photos: LazyPagingItems<Photo>? = null,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    searchQuery: String = "",
    recentSearches: List<SearchQueryEntity> = emptyList(),
    onSearchQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onRemoveRecentSearch: (String) -> Unit = {},
    onClearAllRecentSearches: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var painterImageResult by remember { mutableStateOf<Painter?>(null) }

    SurfaceGradient(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Search Header Card
            SearchHeaderCard(
                searchQuery = searchQuery,
                recentSearches = recentSearches,
                onSearchQueryChange = onSearchQueryChange,
                onSearch = {
                    onSearch(it)
                    keyboardController?.hide()
                },
                onRemoveRecentSearch = onRemoveRecentSearch,
                onClearAllRecentSearches = onClearAllRecentSearches,
                onBackClick = onBackClick
            )

            // Results Section
            when {
                photos != null && searchQuery.isNotEmpty() -> {
                    SearchResultsGrid(
                        photos = photos,
                        isRefreshing = isRefreshing,
                        onRefresh = onRefresh,
                        onImageClick = { painterImageResult = it }
                    )
                }

                searchQuery.isEmpty() -> {
                    EmptyStateText("Enter a search term to find photos")
                }

                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

    if (painterImageResult != null) {
        FullImageView(
            painter = painterImageResult,
            onClickClose = { painterImageResult = null }
        )
    }
}

@Composable
private fun SearchHeaderCard(
    searchQuery: String,
    recentSearches: List<SearchQueryEntity>,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onRemoveRecentSearch: (String) -> Unit,
    onClearAllRecentSearches: () -> Unit,
    onBackClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header with back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(10.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = "Search Photos",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.size(40.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Search TextField
            SearchTextField(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onSearch = onSearch
            )

            // Recent Searches - Only show if no search results
            if (recentSearches.isNotEmpty() && searchQuery.isEmpty()) {
                RecentSearchesSection(
                    recentSearches = recentSearches,
                    onSearch = onSearch,
                    onRemoveRecentSearch = onRemoveRecentSearch,
                    onClearAllRecentSearches = onClearAllRecentSearches
                )
            }
        }
    }
}

@Composable
private fun SearchTextField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        placeholder = {
            Text(
                "Search beautiful photos...",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(searchQuery) }),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = if (searchQuery.isNotEmpty()) {
            {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else null,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun RecentSearchesSection(
    recentSearches: List<SearchQueryEntity>,
    onSearch: (String) -> Unit,
    onRemoveRecentSearch: (String) -> Unit,
    onClearAllRecentSearches: () -> Unit
) {
    Spacer(modifier = Modifier.height(24.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Recent Searches",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        TextButton(
            onClick = onClearAllRecentSearches,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                "Clear All",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(recentSearches.take(4)) { searchEntity ->
            RecentSearchItem(
                searchQuery = searchEntity.searchQuery,
                onSearchClick = { onSearch(searchEntity.searchQuery) },
                onRemoveClick = { onRemoveRecentSearch(searchEntity.searchQuery) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultsGrid(
    photos: LazyPagingItems<Photo>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onImageClick: (Painter) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            items(photos.itemCount) { index ->
                photos[index]?.let { photo ->
                    PhotoItem(
                        photo = photo,
                        onClick = onImageClick
                    )
                }
            }

            when (photos.loadState.append) {
                is LoadState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        }
                    }
                }

                is LoadState.Error -> {
                    item {
                        Text(
                            text = "Error loading more photos",
                            modifier = Modifier.padding(8.dp),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                else -> {}
            }
        }
    }
}

@Composable
private fun EmptyStateText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun RecentSearchItem(
    searchQuery: String,
    onSearchClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickableNoRipple { onSearchClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(16.dp)
            )
            Text(
                text = searchQuery,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium
            )
        }

        IconButton(
            onClick = onRemoveClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun PhotoItem(
    photo: Photo,
    onClick: (Painter) -> Unit
) {
    val painter = rememberAsyncImagePainter(model = photo.src?.medium ?: photo.src?.small)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .bounceClick { onClick(painter) }
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Image(
                painter = painter,
                contentDescription = photo.alt,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((150..220).random().dp)
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = photo.photographer ?: "Unknown",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!photo.alt.isNullOrBlank()) {
                    Text(
                        text = photo.alt,
                        fontSize = 10.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

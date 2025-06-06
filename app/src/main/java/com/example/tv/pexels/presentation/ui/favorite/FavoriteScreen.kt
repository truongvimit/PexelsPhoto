package com.example.tv.pexels.presentation.ui.favorite

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.tv.pexels.core.bounceClick
import com.example.tv.pexels.data.model.room.PhotoEntity
import com.example.tv.pexels.presentation.ui.components.BaseScreenStateHandler
import com.example.tv.pexels.presentation.ui.components.FullImageView
import com.example.tv.pexels.presentation.ui.components.SurfaceGradient
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FavoriteScreen(navController: NavHostController) {
    val favoriteViewModel: FavoriteViewModel = koinViewModel()
    val uiState by favoriteViewModel.stateFlow.collectAsStateWithLifecycle()

    BaseScreenStateHandler(
        loadingState = uiState.loadingState,
        onBackClick = { navController.popBackStack() },
        onRetry = { favoriteViewModel.retry() },
        loadingText = "Loading favorite photos...",
        errorTitle = "Failed to load favorites",
        error = uiState.error
    ) {
        FavoriteSuccessContent(
            favoritePhotos = uiState.favoritePhotos,
            onBackClick = { navController.popBackStack() }
        )
    }
}

@Composable
private fun FavoriteSuccessContent(
    favoritePhotos: List<PhotoEntity>,
    onBackClick: () -> Unit
) {
    var painterImageResult by remember { mutableStateOf<Painter?>(null) }

    SurfaceGradient(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Card
            FavoriteHeaderCard(onBackClick = onBackClick)

            // Content
            when {
                favoritePhotos.isNotEmpty() -> {
                    FavoritePhotosGrid(
                        favoritePhotos = favoritePhotos,
                        onImageClick = { painterImageResult = it }
                    )
                }

                else -> {
                    EmptyFavoritesState()
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
private fun FavoriteHeaderCard(onBackClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Text(
            text = "Favorite Photos",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun FavoritePhotosGrid(
    favoritePhotos: List<PhotoEntity>,
    onImageClick: (Painter) -> Unit
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        items(favoritePhotos) { photoEntity ->
            FavoritePhotoItem(
                photoEntity = photoEntity,
                onClick = onImageClick
            )
        }
    }
}

@Composable
private fun FavoritePhotoItem(
    photoEntity: PhotoEntity,
    onClick: (Painter) -> Unit
) {
    val painter = rememberAsyncImagePainter(
        model = photoEntity.srcMedium ?: photoEntity.srcSmall ?: photoEntity.srcOriginal
    )

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
                contentDescription = photoEntity.alt,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height((150..220).random().dp)
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = photoEntity.photographer ?: "Unknown",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (!photoEntity.alt.isNullOrBlank()) {
                    Text(
                        text = photoEntity.alt,
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

@Composable
private fun EmptyFavoritesState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "❤️",
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "No favorite photos yet",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Swipe right on photos you love to see them here",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 32.dp)
            )
        }
    }
}

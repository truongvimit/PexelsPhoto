package com.example.tv.pexels.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberAsyncImagePainter
import com.example.tv.pexels.R
import com.example.tv.pexels.core.bounceClick
import com.example.tv.pexels.core.clickableNoRipple
import com.example.tv.pexels.data.model.response.Photo
import com.example.tv.pexels.data.model.response.Src
import com.example.tv.pexels.presentation.navigation.Destination
import com.example.tv.pexels.presentation.ui.components.FullImageView
import com.example.tv.pexels.presentation.ui.components.SurfaceGradient
import com.example.tv.pexels.presentation.ui.theme.BaseAllAppTheme
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val homeViewModel: HomeViewModel = koinViewModel()
    val uiState by homeViewModel.stateFlow.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = Modifier.fillMaxSize(),
        photos = uiState.pexelsPhotoPagingData?.collectAsLazyPagingItems(),
        onSearchClick = { navController.navigate(Destination.SearchScreen.fullRoute) },
        onFavouriteClick = { navController.navigate(Destination.FavoriteScreen.fullRoute) },
        onSwipeLeft = { photo -> homeViewModel.dislikePhoto(photo) },
        onSwipeRight = { photo -> homeViewModel.likePhoto(photo) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<Photo>? = null,
    onSearchClick: () -> Unit = {/* no-op */ },
    onFavouriteClick: () -> Unit = {/* no-op */ },
    onSwipeLeft: (Photo) -> Unit = { /* no-op */ },
    onSwipeRight: (Photo) -> Unit = { /* no-op */ }
) {
    var painterImageResult by remember {
        mutableStateOf<Painter?>(null)
    }
    SurfaceGradient(
        modifier = modifier,
    ) {
        if (photos != null) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .zIndex(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onFavouriteClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_favourite),
                                contentDescription = "Search",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Text(
                            text = "Curated Photos",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White,
                            fontSize = 30.sp,
                            textAlign = TextAlign.Center
                        )
                        IconButton(
                            onClick = onSearchClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
                TinderCardStack(
                    photos = photos,
                    modifier = Modifier
                        .fillMaxSize(),
                    onSwipeLeft = onSwipeLeft,
                    onSwipeRight = onSwipeRight,
                    onImageClick = { painter ->
                        painterImageResult = painter
                    }
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading photos...")
            }
        }
    }

    if (painterImageResult != null) {
        FullImageView(
            painter = painterImageResult,
            onClickClose = {
                painterImageResult = null
            },
        )
    }
}

@Composable
fun TinderCardStack(
    photos: LazyPagingItems<Photo>,
    modifier: Modifier = Modifier,
    onSwipeLeft: (Photo) -> Unit,
    onSwipeRight: (Photo) -> Unit,
    onImageClick: (Painter) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var triggerAnimation by remember { mutableStateOf<SwipeDirection?>(null) }

    // Create a custom pager-like state
    val pagerState = remember {
        TinderPagerState(
            initialPage = 0,
            pageCount = { photos.itemCount }
        )
    }

    LaunchedEffect(currentIndex) {
        pagerState.animateScrollToPage(currentIndex)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Use HorizontalPager-like approach but with stacked cards
        TinderPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 30.dp, end = 10.dp, bottom = 90.dp),
            pageCount = photos.itemCount,
            beyondBoundsPageCount = 2 // Similar to beyondViewportPageCount
        ) { pageIndex ->
            val photo = photos[pageIndex]
            if (photo != null) {
                val isTopCard = pageIndex == currentIndex
                val stackIndex = pageIndex - currentIndex

                if (stackIndex >= 0 && stackIndex < 3) { // Show only 3 cards max
                    SwipeablePhotoCard(
                        photo = photo,
                        modifier = Modifier
                            .fillMaxSize()
                            .zIndex((3 - stackIndex).toFloat()),
                        isTopCard = isTopCard,
                        stackIndex = stackIndex,
                        triggerAnimation = if (isTopCard) triggerAnimation else null,
                        onSwipeComplete = { direction ->
                            if (direction == SwipeDirection.LEFT) {
                                onSwipeLeft(photo)
                            } else {
                                onSwipeRight(photo)
                            }
                            currentIndex++
                            triggerAnimation = null
                        },
                        onImageClick = onImageClick
                    )
                }
            }
        }

        if (currentIndex >= photos.itemCount) {
            when (photos.loadState.append) {
                is LoadState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = Color.White,
                    )
                }

                is LoadState.Error -> {
                    Text(
                        text = "Error loading photos",
                        modifier = Modifier.padding(16.dp),
                        color = Color.White
                    )
                }

                else -> {
                    Text(
                        text = "No more photos",
                        modifier = Modifier.padding(16.dp),
                        color = Color.White
                    )
                }
            }
        }

        if (currentIndex < photos.itemCount) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(48.dp)
            ) {
                IconButton(
                    onClick = {
                        triggerAnimation = SwipeDirection.LEFT
                    },
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            Color.White,
                            RoundedCornerShape(32.dp)
                        )
                        .bounceClick()
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Pass",
                        tint = Color.Red,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(
                    onClick = {
                        triggerAnimation = SwipeDirection.RIGHT
                    },
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            Color.Red,
                            RoundedCornerShape(32.dp)
                        )
                        .bounceClick()
                ) {
                    Text(
                        text = "❤️",
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

enum class SwipeDirection {
    LEFT, RIGHT
}

@Composable
private fun SwipeablePhotoCard(
    photo: Photo,
    modifier: Modifier = Modifier,
    isTopCard: Boolean = false,
    stackIndex: Int,
    triggerAnimation: SwipeDirection? = null,
    onSwipeComplete: (SwipeDirection) -> Unit,
    onImageClick: (Painter) -> Unit
) {
    val offsetX = remember(photo.id) { Animatable(0f) }
    val offsetY = remember(photo.id) { Animatable(0f) }
    val rotation = remember(photo.id) { Animatable(0f) }
    val scale = remember(photo.id) { Animatable(1f - (stackIndex * 0.08f)) }
    val translationY = remember(photo.id) { Animatable(stackIndex * 20f) }
    val coroutineScope = rememberCoroutineScope()
    var isSwipeInProgress by remember(photo.id) { mutableStateOf(false) }

    val painter = rememberAsyncImagePainter(
        model = photo.src?.large ?: photo.src?.medium ?: photo.src?.small
    )

    // Animate cards moving up when top card is swiped
    LaunchedEffect(stackIndex) {
        coroutineScope.launch {
            // Animate scale
            val targetScale = if (isTopCard) 1f else 1f - (stackIndex * 0.08f)
            scale.animateTo(
                targetValue = targetScale,
                animationSpec = tween(400)
            )
        }
        coroutineScope.launch {
            // Animate vertical position - cards move up and scale down
            val targetY = if (isTopCard) 0f else -(stackIndex * 25f)
            translationY.animateTo(
                targetValue = targetY,
                animationSpec = tween(400)
            )
        }
    }

    // Unified animation function
    suspend fun performSwipeAnimation(direction: SwipeDirection) {
        isSwipeInProgress = true
        val targetX = if (direction == SwipeDirection.LEFT) -2000f else 2000f
        val targetRotation = if (direction == SwipeDirection.LEFT) -30f else 30f

        coroutineScope.launch {
            offsetX.animateTo(targetX, animationSpec = tween(500))
        }
        coroutineScope.launch {
            rotation.animateTo(targetRotation, animationSpec = tween(500))
        }
        coroutineScope.launch {
            scale.animateTo(0.8f, animationSpec = tween(500))
        }
        kotlinx.coroutines.delay(300)
        onSwipeComplete(direction)
    }

    // Trigger animation from button click
    LaunchedEffect(triggerAnimation) {
        triggerAnimation?.let { direction ->
            coroutineScope.launch {
                performSwipeAnimation(direction)
            }
        }
    }

    Card(
        modifier = modifier
            .offset {
                IntOffset(
                    offsetX.value.roundToInt(),
                    (offsetY.value + translationY.value).roundToInt()
                )
            }
            .graphicsLayer {
                rotationZ = rotation.value
                scaleX = scale.value
                scaleY = scale.value
                transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0.5f, 0f)
            }
            .clickableNoRipple {
                if (isTopCard && !isSwipeInProgress) {
                    onImageClick(painter)
                }
            }
            .pointerInput(photo.id, isTopCard) {
                if (isTopCard) {
                    detectDragGestures(
                        onDragStart = { isSwipeInProgress = true },
                        onDragEnd = {
                            coroutineScope.launch {
                                when {
                                    offsetX.value > 100f -> performSwipeAnimation(SwipeDirection.RIGHT)
                                    offsetX.value < -100f -> performSwipeAnimation(SwipeDirection.LEFT)
                                    else -> {
                                        // Snap back to center
                                        launch { offsetX.animateTo(0f, tween(300)) }
                                        launch { offsetY.animateTo(0f, tween(300)) }
                                        launch { rotation.animateTo(0f, tween(300)) }
                                        launch {
                                            scale.animateTo(
                                                if (isTopCard) 1f else 1f - (stackIndex * 0.08f),
                                                tween(300)
                                            )
                                        }
                                        isSwipeInProgress = false
                                    }
                                }
                            }
                        }
                    ) { _, dragAmount ->
                        if (isTopCard) {
                            coroutineScope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount.x)
                                offsetY.snapTo(offsetY.value + dragAmount.y * 0.3f)

                                val rotationValue = (offsetX.value / 800f) * 30f
                                rotation.snapTo(rotationValue.coerceIn(-30f, 30f))

                                val scaleValue =
                                    1f - (abs(offsetX.value) / 5000f).coerceAtMost(0.03f)
                                scale.snapTo(scaleValue)
                            }
                        }
                    }
                }
            }
            .clip(RoundedCornerShape(30.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isTopCard) 8.dp else (6 - stackIndex).dp
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painter,
                contentDescription = photo.alt,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Swipe indicators - only show when actively swiping
            if (isTopCard && isSwipeInProgress) {
                SwipeIndicators(offsetX = offsetX.value)
            }
        }
    }
}

@Composable
private fun SwipeIndicators(offsetX: Float) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Like indicator
        AnimatedVisibility(
            visible = offsetX > 100f,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
        ) {
            SwipeIndicatorCard(
                text = "LIKE ❤️",
                backgroundColor = Color.Green
            )
        }

        // Pass indicator
        AnimatedVisibility(
            visible = offsetX < -100f,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
        ) {
            SwipeIndicatorCard(
                text = "PASS ❌",
                backgroundColor = Color.Red
            )
        }
    }
}

@Composable
private fun SwipeIndicatorCard(
    text: String,
    backgroundColor: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun TinderPager(
    state: TinderPagerState,
    modifier: Modifier = Modifier,
    pageCount: Int,
    beyondBoundsPageCount: Int = 0,
    content: @Composable (pageIndex: Int) -> Unit
) {
    Box(modifier = modifier) {
        // Render pages similar to HorizontalPager but stacked
        val startIndex = maxOf(0, state.currentPage - beyondBoundsPageCount)
        val endIndex = minOf(pageCount - 1, state.currentPage + beyondBoundsPageCount + 2)

        for (pageIndex in startIndex..endIndex) {
            key(pageIndex) {
                content(pageIndex)
            }
        }
    }
}

class TinderPagerState(
    initialPage: Int = 0,
    private val pageCount: () -> Int
) {
    var currentPage by mutableIntStateOf(initialPage)
        private set

    fun animateScrollToPage(page: Int) {
        if (page < pageCount()) {
            currentPage = page
        }
    }
}

@Composable
@Preview(name = "Home")
private fun HomeScreenPreview() {
    BaseAllAppTheme {
        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            photos = flowOf(
                PagingData.from(listOf(mockPhoto, mockPhoto))
            ).collectAsLazyPagingItems(),
            onSearchClick = {}
        )
    }
}

val mockPhoto = Photo(
    id = 1,
    width = 100,
    height = 100,
    url = "https://example.com",
    photographer = "John Doe",
    photographerUrl = "https://example.com",
    photographerId = 1,
    avgColor = "#000000",
    src = Src(
        original = "https://example.com",
        large = "https://example.com",
        medium = "https://example.com",
        small = "https://example.com",
        portrait = "https://example.com",
        landscape = "https://example.com",
        tiny = "https://example.com"
    ),
    liked = false
)

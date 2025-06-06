package com.example.tv.pexels.presentation.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun BaseScreenStateHandler(
    loadingState: LoadingState,
    onBackClick: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    loadingText: String = "Loading...",
    errorTitle: String = "Failed to load",
    error: String? = null,
    showBackButton: Boolean = true,
    successContent: @Composable () -> Unit
) {
    AnimatedContent(
        targetState = loadingState,
        transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith
                    fadeOut(animationSpec = tween(300))
        },
        label = "BaseScreenStateTransition",
        modifier = modifier
    ) { state ->
        when (state) {
            LoadingState.Loading -> {
                CommonLoadingScreen(
                    onBackClick = onBackClick,
                    loadingText = loadingText,
                    showBackButton = showBackButton
                )
            }

            LoadingState.Success -> {
                successContent()
            }

            LoadingState.Error -> {
                CommonErrorScreen(
                    error = error ?: "Unknown error",
                    onBackClick = onBackClick,
                    onRetry = onRetry,
                    errorTitle = errorTitle,
                    showBackButton = showBackButton
                )
            }
        }
    }
}

enum class LoadingState {
    Loading,
    Success,
    Error
}
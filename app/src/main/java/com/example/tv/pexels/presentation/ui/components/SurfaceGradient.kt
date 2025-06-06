package com.example.tv.pexels.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.tv.pexels.presentation.ui.theme.BackgroundColorAppFirst
import com.example.tv.pexels.presentation.ui.theme.BackgroundColorAppSecond

@Composable
fun SurfaceGradient(
    modifier: Modifier = Modifier,
    brush: Brush = Brush.verticalGradient(
        colors = listOf(
            BackgroundColorAppFirst,
            BackgroundColorAppSecond
        )
    ),
    content: @Composable () -> Unit = {/* no-op */ }
) {
    Surface(
        modifier = modifier,
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = brush
                )
                .statusBarsPadding()
        ) {
            content()
        }
    }
}

@Preview(name = "SurfaceGradient")
@Composable
private fun PreviewSurfaceGradient() {
    SurfaceGradient(
        modifier = Modifier,
        brush = Brush.verticalGradient(
            colors = listOf(Color.Red, Color.Blue)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green)
        )
    }
}
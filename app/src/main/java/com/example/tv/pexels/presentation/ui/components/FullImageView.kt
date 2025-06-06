package com.example.tv.pexels.presentation.ui.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tv.pexels.core.clickableNoRipple
import com.example.tv.pexels.presentation.ui.theme.BaseAllAppTheme

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FullImageView(
    painter: Painter? = null,
    modifier: Modifier = Modifier,
    onClickClose: () -> Unit = { /* no-op */ },
) {
    painter ?: return
    // set up all transformation states
    var scale by remember { mutableStateOf(1f) }
    var rotation by remember { mutableStateOf(0f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
        scale *= zoomChange
        rotation += rotationChange
        offset += offsetChange
    }
    Surface(modifier, color = Color.Transparent) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickableNoRipple {
                    onClickClose()
                }
                .background(Color.Black.copy(0.7f))
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                onClickClose()
                            }
                        )
                    }
                    // apply other transformations like rotation and zoom
                    // on the pizza slice emoji
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        rotationZ = rotation,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    // add transformable to listen to multitouch transformation events
                    // after offset
                    .transformable(state = state)
            )
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(10.dp)
                    .size(30.dp)
                    .clickable(
                        onClick = onClickClose,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .align(Alignment.TopEnd),
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(name = "FullImageView", showSystemUi = true)
@Composable
private fun PreviewFullImageView() {
    BaseAllAppTheme {
        FullImageView(
            modifier = Modifier.fillMaxSize(),
        )
    }
}
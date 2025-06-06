package com.example.tv.pexels.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoDismissDialog(
    isShowDialog: Boolean,
    text: String? = null,
    onDismiss: () -> Unit = {/* no-op */ },
) {
    AnimatedVisibility(
        visible = isShowDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        BasicAlertDialog(
            onDismissRequest = onDismiss,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(10f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = text ?: "",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Black.copy(0.5f))
                        .widthIn(min = 150.dp, max = 200.dp)
                        .padding(12.dp),
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
            }
        }
    }
}
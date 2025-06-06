package com.example.tv.pexels.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.tv.pexels.presentation.ui.theme.ColorAllApp
import com.example.tv.pexels.presentation.ui.theme.GrayColorApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingScreenGif(
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        modifier = modifier,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        ),
        onDismissRequest = { /* no-op */ },
    ) {
        Box(
            modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = GrayColorApp,
                trackColor = ColorAllApp,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview(name = "LoadingScreenGif", showSystemUi = true)
@Composable
private fun PreviewLoadingScreenGif() {
    LoadingScreenGif(
        modifier = Modifier
            .fillMaxSize()
    )
}
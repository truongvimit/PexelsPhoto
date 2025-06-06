package com.example.tv.pexels.presentation.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tv.pexels.R
import com.example.tv.pexels.core.bounceClick
import com.example.tv.pexels.presentation.ui.theme.BackgroundColorApp

@Composable
fun ButtonApp(
    modifier: Modifier = Modifier,
    text: String = "",
    onClick: () -> Unit = {/* no-op */ },
    iconStart: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    isLoadingButton: Boolean = false,
    shape: Shape = CircleShape,
    backgroundColor: Color = BackgroundColorApp,
    textColor: Color = Color.White
) {
    val modifierButton = if (enabled) {
        Modifier
            .clip(
                shape
            )
            .background(backgroundColor)
    } else {
        Modifier
            .clip(
                shape
            )
            .background(Color.Gray)
    }
    val canClick = enabled && isLoadingButton.not()
    Box(
        modifier = modifier
            .let {
                if (canClick) {
                    it.bounceClick(onClick)
                } else {
                    it
                }
            }
            .height(48.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .then(modifierButton)
                .animateContentSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .let {
                        if (isLoadingButton) {
                            it.size(45.dp)
                        } else {
                            it.fillMaxSize()
                        }
                    }
            )
        }
        if (isLoadingButton) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(45.dp)
                    .width(45.dp)
                    .padding(10.dp),
                strokeWidth = 5.dp,
                color = Color.White
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                iconStart?.let {
                    it()
                }
                Text(
                    text = text,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = if (enabled) textColor else Color.LightGray,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Preview(name = "ButtonOrangeApp")
@Composable
private fun PreviewButtonOrangeApp() {
    var isLoading by remember {
        mutableStateOf(false)
    }
    ButtonApp(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        text = stringResource(R.string.app_name),
        iconStart = {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        },
        enabled = true,
        onClick = {
            isLoading = !isLoading
        }
    )
}

package com.example.tv.pexels.presentation.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.tv.pexels.core.NavigationBarUtils
import com.example.tv.pexels.presentation.components.AutoDismissDialog
import com.example.tv.pexels.presentation.components.LoadingScreenGif
import com.example.tv.pexels.presentation.navigation.Destination
import com.example.tv.pexels.presentation.navigation.NavHost
import com.example.tv.pexels.presentation.navigation.SlideDirection
import com.example.tv.pexels.presentation.navigation.composable
import com.example.tv.pexels.presentation.ui.favorite.FavoriteScreen
import com.example.tv.pexels.presentation.ui.home.HomeScreen
import com.example.tv.pexels.presentation.ui.search.SearchScreen
import com.example.tv.pexels.presentation.ui.theme.BackgroundColorApp
import com.example.tv.pexels.presentation.ui.theme.BaseAllAppTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        // Ẩn navigation bar ngay từ đầu
        NavigationBarUtils.hideNavigationBar(this, isImmersiveSticky = true)
        enableEdgeToEdge()
        setContent {
            val navController: NavHostController = rememberNavController()
            val notificationViewModel: NotificationViewModel = koinViewModel()
            val notificationState by notificationViewModel.stateFlow.collectAsStateWithLifecycle()

            BaseAllAppTheme {
                Scaffold(
                    modifier = Modifier.Companion
                        .background(BackgroundColorApp),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    containerColor = Color.Black
                ) { scaffoldPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(scaffoldPadding)
                            .navigationBarsPadding(),
                        color = BackgroundColorApp
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .zIndex(1f)
                        ) {
                            NavigationManager(
                                navController = navController,
                                onFinish = this@MainActivity::finish,
                            )
                        }

                        if (notificationState.isLoading) {
                            BackHandler(
                                enabled = false,
                                onBack = {/* no-op */ }
                            )
                            LoadingScreenGif()
                        }

                        AutoDismissDialog(
                            isShowDialog = notificationState.isShowDialogAuto,
                            text = notificationState.error?.let { stringResource(it) },
                        )
                    }
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        // Đảm bảo immersive mode được duy trì khi người dùng quay lại ứng dụng
        if (hasFocus) {
            NavigationBarUtils.hideNavigationBar(this)
        }
    }
}

@Composable
private fun NavigationManager(
    navController: NavHostController,
    onFinish: () -> Unit = {/* no-op */ },
) {
    NavHost(
        navController = navController, startDestination = Destination.HomeScreen
    ) {
        composable(
            destination = Destination.HomeScreen,
            slideDirection = SlideDirection.LEFT_TO_RIGHT
        ) { HomeScreen(navController) }
        composable(
            destination = Destination.SearchScreen,
            slideDirection = SlideDirection.LEFT_TO_RIGHT,
        ) { SearchScreen(navController) }
        composable(
            destination = Destination.FavoriteScreen,
            slideDirection = SlideDirection.RIGHT_TO_LEFT,
        ) { FavoriteScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BaseAllAppTheme {

    }
}

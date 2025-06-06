package com.example.tv.pexels.presentation.navigation

import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavHost(
    navController: NavHostController,
    startDestination: Destination,
    modifier: Modifier = Modifier,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.fullRoute,
        modifier = modifier,
        route = route,
        builder = builder
    )
}

// Optimized animation timing and easing
private val customEasing = CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f)
private const val ANIMATION_DURATION = 300

enum class SlideDirection {
    LEFT_TO_RIGHT,  // Slide từ trái qua phải
    RIGHT_TO_LEFT   // Slide từ phải qua trái
}

fun NavGraphBuilder.composable(
    destination: Destination,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    slideDirection: SlideDirection = SlideDirection.LEFT_TO_RIGHT,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = destination.fullRoute,
        arguments = arguments,
        deepLinks = deepLinks,
        content = content,
        enterTransition = {
            Log.d("Transition", "enterTransition: $destination")
            when (slideDirection) {
                SlideDirection.LEFT_TO_RIGHT -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                )

                SlideDirection.RIGHT_TO_LEFT -> slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                )
            }
        },
        exitTransition = {
            Log.d("Transition", "exitTransition: $destination - $initialState - $targetState")
            val initialState = initialState.destination.route
            val targetState = targetState.destination.route
            if (targetState == Destination.FavoriteScreen.fullRoute &&
                initialState == Destination.HomeScreen.fullRoute
            ) {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                )
            } else {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                )
            }
        },
        popEnterTransition = {
            // Kiểm tra xem đang quay về Home từ đâu
            val sourceRoute = initialState.destination.route
            when {
                // Quay về Home: check màn nguồn để lấy backToHomeDirection
                targetState.destination.route == Destination.HomeScreen.fullRoute -> {
                    when (sourceRoute) {
                        Destination.SearchScreen.fullRoute -> {
                            // SearchScreen có backToHomeDirection.RIGHT_TO_LEFT
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                            )
                        }

                        Destination.FavoriteScreen.fullRoute -> {
                            // FavoriteScreen có backToHomeDirection.LEFT_TO_RIGHT  
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                            )
                        }

                        else -> {
                            // Mặc định AUTO
                            slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                            )
                        }
                    }
                }
                // Các trường hợp khác: slide ngược lại
                else -> {
                    when (slideDirection) {
                        SlideDirection.LEFT_TO_RIGHT -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                        )

                        SlideDirection.RIGHT_TO_LEFT -> slideIntoContainer(
                            AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                        )
                    }
                }
            }
        },
        popExitTransition = {
            when (slideDirection) {
                SlideDirection.LEFT_TO_RIGHT -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                )

                SlideDirection.RIGHT_TO_LEFT -> slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(ANIMATION_DURATION, easing = customEasing)
                )
            }
        }
    )
}

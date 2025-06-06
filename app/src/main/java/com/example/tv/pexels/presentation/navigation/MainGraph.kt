package com.example.tv.pexels.presentation.navigation


sealed class Destination(protected val route: String, vararg params: String) {
    val fullRoute: String = if (params.isEmpty()) route else {
        val builder = StringBuilder(route)
        params.forEach { builder.append("/{${it}}") }
        builder.toString()
    }

    data object SplashScreen : Destination("splash_screen")
    data object LanguageScreen : Destination("language_screen", "fromWhere"){
        private const val FIST_NAME_KEY = "fromWhere"

        operator fun invoke(fromWhere: String): String = route.appendParams(
            FIST_NAME_KEY to fromWhere,
        )
    }
    data object OnboardingScreen : Destination("onboarding_screen")
    data object OnboardingBackScreen : Destination("onboarding_back_screen")

    data object ChooseStyleScreen : Destination("choose_style_screen")
    data object PermissionAllScreen : Destination("permission_all_screen", "fromWhere"){
        private const val FIST_NAME_KEY = "fromWhere"

        operator fun invoke(fromWhere: String): String = route.appendParams(
            FIST_NAME_KEY to fromWhere,
        )
    }
    data object HomeScreen : Destination("home_screen")
    data object SearchScreen : Destination("search_screen")
    data object FavoriteScreen : Destination("favorite_screen")
    data object TutorialSelectPhotoScreen :
        Destination("tutorial_select_photo_screen", "objectJson") {
        private const val FIST_NAME_KEY = "objectJson"

        operator fun invoke(objectJson: String): String = route.appendParams(
            FIST_NAME_KEY to objectJson,
        )
    }
    data object PhotoPickerScreen : Destination("photo_picker_screen")
    data object CropImageScreen : Destination("crop_image_screen", "uriImage") {
        private const val FIST_NAME_KEY = "uriImage"

        operator fun invoke(uriImage: String): String = route.appendParams(
            FIST_NAME_KEY to uriImage,
        )
    }

    data object GenerateSimilarScreen : Destination("generate_similar_screen", "uriImage") {
        private const val FIST_NAME_KEY = "uriImage"

        operator fun invoke(uriImage: String): String = route.appendParams(
            FIST_NAME_KEY to uriImage,
        )
    }

    data object HistoryScreen : Destination("history_screen")
    data object SettingsScreen : Destination("settings_screen")


    data object ResultScreen : Destination("result_screen", "objectJson") {
        private const val FIST_NAME_KEY = "objectJson"

        operator fun invoke(objectJson: String): String = route.appendParams(
            FIST_NAME_KEY to objectJson,
        )
    }
}

internal fun String.appendParams(vararg params: Pair<String, Any?>): String {
    val builder = StringBuilder(this)

    params.forEach {
        it.second?.toString()?.let { arg ->
            builder.append("/$arg")
        }
    }

    return builder.toString()
}

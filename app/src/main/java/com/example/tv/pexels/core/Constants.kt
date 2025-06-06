package com.example.tv.pexels.core

object Constants {

    object MainApi {
        const val NAME = "Pexels"
        const val BASE_URL: String = "https://api.pexels.com/"
        const val API_KEY: String = "ossgj0vPdMxA54CYboNEnID3eQLt0BFpWth51dS0vUehch41zsgTYAU5"
    }

    object Queries {
        const val GET_ALL_SEARCH_QUERIES =
            "SELECT * FROM search_query_table ORDER BY createdAt DESC"
        const val DELETE_SEARCH_QUERY_BY_ID =
            "DELETE FROM search_query_table WHERE searchQuery = :searchQuery"

        const val GET_ALL_LIKED_PHOTOS =
            "SELECT * FROM photo_table WHERE isLiked = 1 ORDER BY createdAt DESC"
        const val GET_ALL_DISLIKED_PHOTOS =
            "SELECT * FROM photo_table WHERE isLiked = 0 ORDER BY createdAt DESC"
        const val DELETE_PHOTO_BY_ID =
            "DELETE FROM photo_table WHERE id = :id"
        const val GET_PHOTO_BY_ID =
            "SELECT * FROM photo_table WHERE id = :id"
        const val GET_ALL_PHOTOS =
            "SELECT * FROM photo_table ORDER BY createdAt DESC"
    }
}

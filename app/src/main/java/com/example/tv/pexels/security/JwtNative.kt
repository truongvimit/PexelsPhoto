// JwtNative.kt
package com.example.tv.pexels.security

class JwtNative {
    init {
        System.loadLibrary("jwt-native")
    }

    external fun generateToken(userId: String, username: String, expiryMinutes: Int): String
    external fun validateToken(token: String): Boolean
}
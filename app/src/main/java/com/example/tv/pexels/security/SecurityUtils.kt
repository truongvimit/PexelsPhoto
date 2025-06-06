// SecurityUtils.kt
package com.example.tv.pexels.security

object SecurityUtils {
    fun isEmulator(): Boolean {
        return android.os.Build.FINGERPRINT.startsWith("generic") 
                || android.os.Build.FINGERPRINT.startsWith("unknown") 
                || android.os.Build.MODEL.contains("google_sdk") 
                || android.os.Build.MODEL.contains("Emulator") 
                || android.os.Build.MODEL.contains("Android SDK built for x86") 
                || android.os.Build.MANUFACTURER.contains("Genymotion") 
                || (android.os.Build.BRAND.startsWith("generic") && android.os.Build.DEVICE.startsWith("generic")) 
                || "google_sdk" == android.os.Build.PRODUCT
    }

    fun isRooted(): Boolean {
        val rootFiles = arrayOf(
            "/system/app/Superuser.apk",
            "/system/xbin/su",
            "/system/bin/su",
            "/sbin/su",
            "/system/su",
            "/system/bin/.ext/.su"
        )

        for (file in rootFiles) {
            if (java.io.File(file).exists()) return true
        }
        
        return false
    }
    
    fun isSecureEnvironment(): Boolean {
        return !isEmulator() && !isRooted()
    }
}
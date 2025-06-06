package com.example.tv.pexels.di

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException

class CustomInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            val original = chain.request()
            chain.proceed(original)
        } catch (e: IOException) {
            // Trả về một Response lỗi để tránh crash app
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1) // Sử dụng HTTP/1.1 như là mặc định
                .code(500) // Mã lỗi (có thể tùy chỉnh)
                .message("Cannot connect to server") // Thông điệp lỗi
                .body("Error: ${e.message}".toResponseBody(null)) // Nội dung lỗi (có thể tùy chỉnh)
                .build()
        }
    }
}
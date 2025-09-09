package com.example.food_order.data.api

import com.example.food_order.manager.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionManager: SessionManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        // Lấy token từ SessionManager
        sessionManager.fetchAuthToken()?.let { token ->
            // Nếu có token, thêm vào header
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(requestBuilder.build())
    }
}
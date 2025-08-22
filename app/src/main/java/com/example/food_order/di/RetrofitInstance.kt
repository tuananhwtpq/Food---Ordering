package com.example.food_order.di

import android.content.Context
import com.example.food_order.data.api.AuthApiService
import com.example.food_order.data.api.AuthInterceptor
import com.example.food_order.data.api.MainApiService
import com.example.food_order.manager.SessionManager
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.1.25:8080/"

    val authApi: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            ) // Client đơn giản
            .build()
            .create(AuthApiService::class.java)
    }

    fun getMainApi(context: Context): MainApiService {
        val sessionManager = SessionManager(context)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)) // Để debug log
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MainApiService::class.java)
    }
}
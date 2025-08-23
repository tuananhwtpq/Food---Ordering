package com.example.food_order.di

import android.content.Context
import com.example.food_order.data.api.AuthApiService
import com.example.food_order.data.api.AuthInterceptor
import com.example.food_order.data.api.MainApiService
import com.example.food_order.di.AppModule_ProvideSessionManagerFactory.provideSessionManager
import com.example.food_order.manager.SessionManager
import com.example.food_order.utils.Constants

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val authApi: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .build()
            .create(AuthApiService::class.java)
    }

    fun getMainApi(context: Context): MainApiService {
        val sessionManager = SessionManager(context)

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(MainApiService::class.java)
    }


    /** Tạo service bất kỳ (ví dụ MenuApiService) nhưng vẫn kèm AuthInterceptor */
    inline fun <reified T> createAuthorizedServiceGeneric(context: Context): T {
        // Nếu dự án đã có SessionManager + AuthInterceptor cho flow login, dùng lại:
        val sessionManager = SessionManager(context)       // <— bạn đã có hàm này
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(sessionManager))  // <— dùng đúng interceptor hiện có
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)                           // <— giữ đúng BASE_API của dự án
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(T::class.java)
    }
}
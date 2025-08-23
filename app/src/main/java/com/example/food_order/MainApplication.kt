package com.example.food_order

import android.app.Application
import com.example.food_order.data.api.AuthApiService
import com.example.food_order.data.repository.AuthRepository
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.manager.SessionManager

class MainApplication : Application() {

    lateinit var sessionManager: SessionManager
    lateinit var authApiService: AuthApiService
    lateinit var authRepository: AuthRepository

    override fun onCreate() {
        super.onCreate()

        sessionManager = SessionManager(this)
        authApiService = RetrofitInstance.authApi

        authRepository = AuthRepository(authApiService, sessionManager)
    }
}
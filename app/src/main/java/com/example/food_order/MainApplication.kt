package com.example.food_order

import android.app.Application
import com.example.food_order.data.repository.AuthRepository
import com.example.food_order.manager.SessionManager

class MainApplication : Application() {

    lateinit var sessionManager: SessionManager
    lateinit var authRepository: AuthRepository

    override fun onCreate() {
        super.onCreate()

        sessionManager = SessionManager(this)
        authRepository = AuthRepository()
    }
}
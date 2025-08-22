package com.example.food_order.manager

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    companion object {
        const val AUTH_TOKEN = "auth_token"
        const val USER_ROLE = "user_role"
    }

    fun saveAuthDetails(token: String, role: String) {
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, token)
        editor.putString(USER_ROLE, role)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }

    fun fetchUserRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }
}
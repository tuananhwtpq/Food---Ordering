package com.example.food_order.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.food_order.data.model.response.AuthResponse

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    companion object {
        const val AUTH_TOKEN = "auth_token"
        const val USER_ROLE = "user_role"
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
    }

    fun saveAuthDetails(authResponse: AuthResponse) {
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, authResponse.token)
        editor.putString(USER_ROLE, authResponse.role)
        editor.putString(USER_ID, authResponse.userId)
        editor.putString(USER_EMAIL, authResponse.email)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }

    fun fetchUserRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }

    fun fetchUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()

    }
}
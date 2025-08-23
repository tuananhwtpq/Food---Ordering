package com.example.food_order.manager

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    companion object {
        const val AUTH_TOKEN = "auth_token"
        const val USER_ROLE = "user_role"
        const val SELECTED_RESTAURANT_ID = "selected_restaurant_id"   // thêm
        const val SELECTED_RESTAURANT_NAME = "selected_restaurant_name" // (tùy chọn)
    }

    fun saveAuthDetails(token: String, role: String) {
        prefs.edit().apply {
            putString(AUTH_TOKEN, token)
            putString(USER_ROLE, role)
        }.apply()
    }

    fun fetchAuthToken(): String? = prefs.getString(AUTH_TOKEN, null)
    fun fetchUserRole(): String? = prefs.getString(USER_ROLE, null)

    fun saveSelectedRestaurantId(id: String, name: String? = null) {
        prefs.edit().putString(SELECTED_RESTAURANT_ID, id).apply()
        if (name != null) prefs.edit().putString(SELECTED_RESTAURANT_NAME, name).apply()
    }

    fun fetchSelectedRestaurantId(): String? =
        prefs.getString(SELECTED_RESTAURANT_ID, null)

    fun clearSelectedRestaurantId() {
        prefs.edit().remove(SELECTED_RESTAURANT_ID).remove(SELECTED_RESTAURANT_NAME).apply()
    }

}
package com.example.food_order.manager

import android.content.Context
import android.content.SharedPreferences
import com.example.food_order.data.model.common.RestaurantSummary
import com.example.food_order.data.model.response.AuthResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    private val KEY_RESTAURANTS_JSON = "restaurants_json"
    companion object {
        const val AUTH_TOKEN = "auth_token"
        const val USER_ROLE = "user_role"
        const val KEY_CURRENT_RESTAURANT_ID = "current_restaurant_id"   // thêm
        const val KEY_CURRENT_RESTAURANT_NAME = "current_restaurant_name" // (tùy chọn)
        const val USER_ID = "user_id"
        const val USER_EMAIL = "user_email"
        const val USER_NAME = "user_name"
        const val USER_LATITUDE = "user_latitude"
        const val USER_LONGITUDE = "user_longitude"
    }

    fun saveAuthDetails(authResponse: AuthResponse) {
        val editor = prefs.edit()
        editor.putString(AUTH_TOKEN, authResponse.token)
        editor.putString(USER_ROLE, authResponse.role)
        editor.putString(USER_ID, authResponse.userId)
        editor.putString(USER_EMAIL, authResponse.email)
        editor.putString(USER_NAME, authResponse.username)
        editor.apply()
    }
    fun getRestaurants(): List<RestaurantSummary> {
        val json = prefs.getString(KEY_RESTAURANTS_JSON, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<RestaurantSummary>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }
    fun setCurrentRestaurant(id: String, name: String) {
        prefs.edit()
            .putString(KEY_CURRENT_RESTAURANT_ID, id)
            .putString(KEY_CURRENT_RESTAURANT_NAME, name)
            .apply()
    }

    fun getCurrentRestaurantId(): String? =
        prefs.getString(KEY_CURRENT_RESTAURANT_ID, null)

    fun getCurrentRestaurantName(): String? =
        prefs.getString(KEY_CURRENT_RESTAURANT_NAME, null)


    fun saveRestaurants(list: List<RestaurantSummary>) {
        val json = Gson().toJson(list)
        prefs.edit().putString(KEY_RESTAURANTS_JSON, json).apply()
    }

    fun saveAuthDetails(token: String, role: String) {
        prefs.edit().apply {
            putString(AUTH_TOKEN, token)
            putString(USER_ROLE, role)
        }.apply()
    }

    fun saveLocation(latitude: Double, longitude: Double) {
        val editor = prefs.edit()
        editor.putFloat(USER_LATITUDE, latitude.toFloat())
        editor.putFloat(USER_LONGITUDE, longitude.toFloat())
        editor.apply()
    }

    fun fetchUserRole(): String? = prefs.getString(USER_ROLE, null)

    fun saveSelectedRestaurantId(id: String, name: String? = null) {
        prefs.edit().putString(KEY_CURRENT_RESTAURANT_ID, id).apply()
        if (name != null) prefs.edit().putString(KEY_CURRENT_RESTAURANT_NAME, name).apply()
        fun fetchLatitude(): Double? {
            return prefs.getFloat(USER_LATITUDE, 0f).toDouble().takeIf { it != 0.0 }
        }
    }
    fun fetchLatitude(): Double? {
        return prefs.getFloat(USER_LATITUDE, 0f).toDouble().takeIf { it != 0.0 }
    }

    fun fetchLongitude(): Double? {
        return prefs.getFloat(USER_LONGITUDE, 0f).toDouble().takeIf { it != 0.0 }
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(AUTH_TOKEN, null)
    }

    fun fetchSelectedRestaurantId(): String? =
        prefs.getString(KEY_CURRENT_RESTAURANT_ID, null)

    fun clearSelectedRestaurantId() {
        prefs.edit().remove(KEY_CURRENT_RESTAURANT_ID).remove(KEY_CURRENT_RESTAURANT_NAME).apply()
    }

    fun fetchUserEmail(): String? {
        return prefs.getString(USER_EMAIL, null)
    }

    fun fetchUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun fetchUserName(): String? {
        return prefs.getString(USER_NAME, null)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()

    }

}
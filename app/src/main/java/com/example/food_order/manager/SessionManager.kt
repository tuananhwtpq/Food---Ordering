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
        const val USER_NAME = "user_name"
        const val USER_LATITUDE = "user_latitude"
        const val USER_LONGITUDE = "user_longitude"
        const val ADDRESS_ID = "address_id"
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

    fun saveAddressId(addressId: String) {
        val editor = prefs.edit()
        editor.putString(ADDRESS_ID, addressId)
        editor.apply()
    }

    fun fetchAddressId(): String? {
        return prefs.getString(ADDRESS_ID, null)
    }

    fun saveLocation(latitude: Double, longitude: Double) {
        val editor = prefs.edit()
        editor.putFloat(USER_LATITUDE, latitude.toFloat())
        editor.putFloat(USER_LONGITUDE, longitude.toFloat())
        editor.apply()
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

    fun fetchUserRole(): String? {
        return prefs.getString(USER_ROLE, null)
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
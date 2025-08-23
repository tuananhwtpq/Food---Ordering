package com.example.food_order.utils.extension

import android.util.Log
import com.example.food_order.data.model.response.ErrorResponse
import com.google.gson.Gson
import okhttp3.ResponseBody
import java.io.IOException

fun ResponseBody.parseError(): String? {
    return try {
        val gson = Gson()
        val errorResponse = gson.fromJson(this.string(), ErrorResponse::class.java)
        errorResponse?.message
    } catch (e: IOException) {
        Log.e("AuthRepository", "Error parsing error body: ${e.message}", e)
        null
    }
}

fun String.toErrorMessage(): String? {
    return try {
        Gson().fromJson(this, ErrorResponse::class.java)?.message
    } catch (e: Exception) {
        null
    }
}
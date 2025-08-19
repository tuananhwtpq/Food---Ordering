package com.example.food_order.utils.extension

import android.util.Log
import com.example.food_order.utils.feat.Constants

//fun isDebugMode(): Boolean {
//    return BuildConfig.DEBUG
//}

fun logDebug(message: String) {
    Log.d(Constants.TAG, message)
}

fun tryCatch(tryBlock: ()-> Unit, catchBlock: ((e: Exception) -> Unit)? = null){
    try {
        tryBlock()
    } catch (e: Exception){
        catchBlock?.invoke(e)
    }
}
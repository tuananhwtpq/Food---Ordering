package com.example.food_order.utils.extension

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.food_order.R
import com.example.food_order.utils.feat.Constants

fun NavController.safeNavigate(
    actionId: Int,
    bundle: Bundle? = null,
    navOptions: NavOptions? = null
){
    //Kiểm tra action có tồn tại không, nếu không thì lấy
    // từ toàn bộ navGraph
    val action = currentDestination?.getAction(actionId) ?: graph.getAction(actionId)
    //Kiểm tra action có null không
    //Kiểm tra xem action có navigate đến chính mình không
    if(action != null && currentDestination?.id != action.destinationId){
        navigate(actionId, bundle, navOptions)
    }
}

fun Fragment.safeNavigate(
    actionId: Int,
    bundle: Bundle? = null,
    navOptions: NavOptions? = defaultSlideNavOptions()
){
    try {
        val navController = findNavController()
        Log.d("NavGraphDebug", "Current graph: ${navController.graph}, Current dest: ${navController.currentDestination?.label}")
        navController.safeNavigate(actionId, bundle, navOptions)
    } catch (e: Exception){
//        AppLogger.e(
//            Constants.TAG, "${this.javaClass.simpleName} - error ${e.stackTraceToString()}"
//        )
        Log.e("Ngoại lệ: ${this.javaClass.simpleName}", "Lỗi: ${e.stackTraceToString()}")
    }
}

fun Fragment.navigateUp(){
    try {
        findNavController().navigateUp()
    } catch (e: Exception){
        e.printStackTrace()
    }
}

fun defaultSlideNavOptions(): NavOptions {
    return NavOptions.Builder()
        .setEnterAnim(R.anim.slide_in_right)
        .setExitAnim(R.anim.slide_out_left)
        .setPopEnterAnim(R.anim.slide_out_right)
        .setPopExitAnim(R.anim.slide_out_right)
        .build()
}
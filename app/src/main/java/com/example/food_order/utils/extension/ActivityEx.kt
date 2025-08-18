package com.example.food_order.utils.extension

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import com.example.food_order.R

//Xử lý backPress
fun ComponentActivity.handleBackPressed(action: () -> Unit){
    this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            action()
        }
    })
}


fun Activity.finishWithSlide(){
    finish()
    /*Supress là annotation dùng để bỏ qua các cảnh báo (Ở đây là)
        hàm overridePendingTransition() -> Dùng cho các android
        từ Android 14 đổ xuống
     */
    @Suppress("DEPRECATION") overridePendingTransition(
        R.anim.slide_in_right, R.anim.slide_out_left
    )

}
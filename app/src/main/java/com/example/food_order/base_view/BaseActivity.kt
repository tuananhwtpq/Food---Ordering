package com.example.food_order.base_view

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.example.food_order.R
import com.example.food_order.utils.extension.finishWithSlide
import com.example.food_order.utils.extension.handleBackPressed


abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity() {

    //region Variable
    private var _binding: VB? = null
    val binding: VB
        get() = _binding!!

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateViewBinding(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom)
            insets
        }

        initView()
        initData()
        initListener()
        handleBackPressed {
            onBack()
        }
    }


    open fun onBack(){
        if (supportFragmentManager.backStackEntryCount > 0){
            onBackPressedDispatcher.onBackPressed()
        } else {
            finishWithSlide()
        }
    }

    abstract fun inflateViewBinding(inflater: LayoutInflater): VB
    abstract fun initView()
    abstract fun initData()
    abstract fun initListener()

    //region Logcat

    fun logDebug(msg: String){

    }

}
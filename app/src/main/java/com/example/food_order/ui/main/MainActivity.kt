package com.example.food_order.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.food_order.R
import com.example.food_order.base_view.BaseActivity
import com.example.food_order.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {


    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initView() {
        val userRole = intent.getStringExtra("USER_ROLE")

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        val navController = navHostFragment.navController

        if (userRole == "customer") {
            // Load menu của customer
            binding.bottomNavigation.inflateMenu(R.menu.customer_bottom_nav)
        } else { // userRole == "owner"

            // Load menu của owner
            binding.bottomNavigation.inflateMenu(R.menu.owner_bottom_nav)
        }

        // Kết nối BottomNavigationView với NavController
        binding.bottomNavigation.setupWithNavController(navController)

    }

    override fun initData() {

    }

    override fun initListener() {

    }
}
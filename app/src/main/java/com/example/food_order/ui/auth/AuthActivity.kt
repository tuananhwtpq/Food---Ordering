package com.example.food_order.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.food_order.R
import com.example.food_order.databinding.ActivityAuthBinding


class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment

        navController = navHostFragment.navController

        val navigateToLogin = intent.getBooleanExtra("NAVIGATE_TO_LOGIN", false)
        if (navigateToLogin) {
            val navGraph = navController.navInflater.inflate(R.navigation.nav_auth)
            navGraph.setStartDestination(R.id.loginFragment2)
            navController.graph = navGraph
        }

    }

    fun navigateToLogin() {
        navController.navigate(R.id.action_splashFragment_to_loginFragment2)
    }
}
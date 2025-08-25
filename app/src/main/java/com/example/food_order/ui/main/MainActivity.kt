package com.example.food_order.ui.main


import android.view.LayoutInflater
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
        val navGraph = navController.navInflater.inflate(R.navigation.main_nav_graph)
        if (userRole == "customer") {
            navGraph.setStartDestination(R.id.customerHomeFragment)
            binding.bottomNavigation.inflateMenu(R.menu.customer_bottom_nav)
        } else {

            navGraph.setStartDestination(R.id.ownerHomeFragment)

            binding.bottomNavigation.inflateMenu(R.menu.owner_bottom_nav)
        }
        navController.graph = navGraph
        binding.bottomNavigation.setupWithNavController(navController)

    }

    override fun initData() {

    }

    override fun initListener() {

    }
}
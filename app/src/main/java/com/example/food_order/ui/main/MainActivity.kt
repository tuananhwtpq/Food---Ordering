package com.example.food_order.ui.main


import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.food_order.MainApplication
import com.example.food_order.R
import com.example.food_order.base_view.BaseActivity
import com.example.food_order.databinding.ActivityMainBinding
import com.example.food_order.manager.SessionManager
import com.example.food_order.ui.auth.AuthActivity
import com.google.android.material.navigation.NavigationView


class MainActivity : BaseActivity<ActivityMainBinding>(),
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var navController: NavController
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sessionManager: SessionManager

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initView() {

        sessionManager = (application as MainApplication).sessionManager

        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(
            this,
            binding.main,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.main.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        val userRole = intent.getStringExtra("USER_ROLE")
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_main) as NavHostFragment
        navController = navHostFragment.navController
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
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val topLevelDestinations = setOf(
                R.id.customerHomeFragment,
                R.id.cartFragment,
                R.id.customerOrdersFragment,
                R.id.ownerHomeFragment,
                R.id.deliveryFragment,
                R.id.menuRestaurantFragment
            )

            if (destination.id in topLevelDestinations) {
                binding.bottomNavigation.visibility = View.VISIBLE
            } else {
                binding.bottomNavigation.visibility = View.GONE
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                onBackPressed()
            }

            R.id.nav_settings -> {
                Toast.makeText(this, "Cài đặt", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_about -> {
                Toast.makeText(this, "Về chúng tôi", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_logout -> {
                handleLogout()
            }
        }
        binding.main.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.main.isDrawerOpen(GravityCompat.START)) {
            binding.main.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun handleLogout() {
        sessionManager.clearSession()

        val intent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("NAVIGATE_TO_LOGIN", true)
        }
        startActivity(intent)

        finish()
    }
}
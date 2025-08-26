package com.example.food_order.ui.main


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView


class MainActivity : BaseActivity<ActivityMainBinding>(),
    NavigationView.OnNavigationItemSelectedListener {

    private val TAG = "MainActivity"

    private lateinit var navController: NavController
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var sessionManager: SessionManager
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            getUserLocation()
        } else {
            Toast.makeText(
                this,
                "Quyền vị trí bị từ chối. Sẽ sử dụng vị trí mặc định.",
                Toast.LENGTH_SHORT
            ).show()
            saveDefaultLocation()
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityMainBinding {
        return ActivityMainBinding.inflate(inflater)
    }

    override fun initView() {

        sessionManager = (application as MainApplication).sessionManager
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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

        updateUserInfo()
        setupNavigation()
        requestLocationPermission()
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
                Toast.makeText(this, "Chức năng này hiện tại đang bị khóa", Toast.LENGTH_SHORT).show()
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

    private fun updateUserInfo() {
        val headerView = binding.navView.getHeaderView(0)
        val usernameTextView = headerView.findViewById<TextView>(R.id.nav_header_username)
        val emailTextView = headerView.findViewById<TextView>(R.id.nav_header_email)

        val userEmail = sessionManager.fetchUserEmail()
        val username = sessionManager.fetchUserName() ?: "Người dùng"

        usernameTextView.text = username
        emailTextView.text = userEmail ?: "Không có email"
    }

    private fun setupNavigation() {
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

    private fun requestLocationPermission() {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineLocationGranted || coarseLocationGranted) {
            getUserLocation()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showPermissionRationale()
            } else {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun showPermissionRationale() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Quyền vị trí")
            .setMessage("Chúng tôi cần quyền truy cập vị trí để tìm nhà hàng gần bạn. Vui lòng cho phép.")
            .setPositiveButton("OK") { _, _ ->
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            .setNegativeButton("Hủy") { _, _ ->
                saveDefaultLocation()
            }
            .show()
    }

    private fun getUserLocation() {
        if (!isLocationEnabled()) {
            Toast.makeText(this, "Vui lòng bật GPS để lấy vị trí chính xác.", Toast.LENGTH_SHORT)
                .show()
            saveDefaultLocation()
            return
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    Log.d(TAG, "Vị trí tọa độ: Lat: $latitude, Lon: $longitude")


                    sessionManager.saveLocation(latitude, longitude)
                    showToast("Vị trí của bạn đã được cập nhật.")
                } else {
                    saveDefaultLocation()
                }
            }.addOnFailureListener {
                saveDefaultLocation()
            }
        } else {
            saveDefaultLocation()
        }
    }

    private fun saveDefaultLocation() {
        val defaultLat = 21.0278
        val defaultLon = 105.8342
        sessionManager.saveLocation(defaultLat, defaultLon)
        Log.d(TAG, "Sử dụng vị trí mặc định: Hà Nội (Lat: $defaultLat, Lon: $defaultLon)")
        Toast.makeText(this, "Sử dụng vị trí mặc định: Hà Nội.", Toast.LENGTH_SHORT).show()
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}
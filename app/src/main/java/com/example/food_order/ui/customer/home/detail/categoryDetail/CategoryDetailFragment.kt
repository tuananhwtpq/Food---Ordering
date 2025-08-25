package com.example.food_order.ui.customer.home.detail.categoryDetail

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.CategoryApiServices
import com.example.food_order.data.api.RestaurantApiService
import com.example.food_order.data.repository.CategoryRepository
import com.example.food_order.data.repository.RestaurantRepository
import com.example.food_order.databinding.FragmentCategoryDetailBinding
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.ui.adapter.RestaurantListAdapter
import com.example.food_order.utils.extension.launchOnStarted
import com.example.food_order.utils.extension.safeNavigate
import com.example.food_order.utils.extension.showToast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class CategoryDetailFragment : BaseFragment<FragmentCategoryDetailBinding>() {

    private val args: CategoryDetailFragmentArgs by navArgs()

    private val viewModel: CategoryDetailViewModel by viewModels {
        val context = requireContext().applicationContext
        val apiService = RetrofitInstance.create(context, RestaurantApiService::class.java)
        val repository = RestaurantRepository(apiService)
        CategoryDetailViewModelFactory(repository)
    }

    private val restaurantAdapter = RestaurantListAdapter { restaurant ->
        navigateToRestaurantDetail(restaurant.id)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getUserLocation()
        } else {
            showToast("Quyền vị trí bị từ chối, sử dụng vị trí mặc định")
            fetchRestaurantsWithFallback()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryDetailBinding {
        return FragmentCategoryDetailBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRestaurants.adapter = restaurantAdapter
        val categoryId = args.categoryId
        val categoryName = args.categoryName

        binding.toolbar.title = categoryName
        viewModel.setCategoryName(categoryName)

        // Kiểm tra và yêu cầu quyền vị trí
        if (checkLocationPermission()) {
            getUserLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        when {
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showToast("Quyền vị trí cần thiết để tìm nhà hàng gần bạn.")
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    override fun observeData() {
        super.observeData()

        launchOnStarted {
            viewModel.uiState.collect { uiState ->
                uiState.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }

                if (!uiState.isLoading) {
                    restaurantAdapter.setData(uiState.restaurants)
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()
        binding.toolbar.setNavigationOnClickListener {
            onBack()
        }
    }

    private fun getUserLocation() {
        if (!isLocationEnabled()) {
            showToast("Vui lòng bật GPS để lấy vị trí.")
            fetchRestaurantsWithFallback()
            return
        }

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("CategoryDetailFragment", "Tọa độ của bạn: $latitude, $longitude")
                    viewModel.fetchRestaurantsByCategoryId(args.categoryId, latitude, longitude)
                    lifecycleScope.launch {
                        fetchAddressFromCoordinates(latitude, longitude)
                    }
                } else {
                    showToast("Không lấy được vị trí, sử dụng vị trí mặc định")
                    fetchRestaurantsWithFallback()
                }
            }.addOnFailureListener { exception ->
                showToast("Lỗi lấy vị trí: ${exception.message}")
                fetchRestaurantsWithFallback()
            }
        } catch (e: SecurityException) {
            showToast("Quyền vị trí bị từ chối")
            fetchRestaurantsWithFallback()
        }
    }

    private fun fetchRestaurantsWithFallback() {
        val defaultLat = 21.0278
        val defaultLon = 105.8342
        viewModel.fetchRestaurantsByCategoryId(args.categoryId, defaultLat, defaultLon)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private suspend fun fetchAddressFromCoordinates(latitude: Double, longitude: Double) {
        withContext(Dispatchers.IO) {
            try {
                val url =
                    "https://nominatim.openstreetmap.org/reverse?lat=$latitude&lon=$longitude&format=json&addressdetails=1"
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .header(
                        "User-Agent",
                        "FoodOrderApp/1.0 (your.email@example.com)"
                    ) // Tuân thủ quy định Nominatim
                    .build()
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val json = JSONObject(response.body?.string() ?: "")
                    val address = json.optString("display_name", "Không xác định")
                    Log.d("CategoryDetailFragment", "Địa chỉ của bạn: $address")
                    // Có thể hiển thị toast nếu muốn
                    // showToast("Địa chỉ của bạn: $address")
                } else {
                    Log.w(
                        "CategoryDetailFragment",
                        "Không thể lấy địa chỉ, mã lỗi: ${response.code}"
                    )
                }
            } catch (e: Exception) {
                Log.e("CategoryDetailFragment", "Lỗi lấy địa chỉ: ${e.message}")
            }
        }
    }

    override fun onBack() {
        super.onBack()
    }

    private fun navigateToRestaurantDetail(restaurantId: String) {
        val bundle = bundleOf(
            "restaurantId" to restaurantId
        )
        safeNavigate(R.id.action_categoryDetailFragment_to_restaurantDetailFragment, bundle)
    }

}
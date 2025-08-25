package com.example.food_order.ui.customer.home.detail.restaurantDetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.RestaurantApiService
import com.example.food_order.data.repository.RestaurantRepository
import com.example.food_order.databinding.FragmentRestaurantDetailBinding
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.ui.adapter.MenuAdapter
import com.example.food_order.utils.extension.launchOnStarted
import com.example.food_order.utils.extension.safeNavigate

class RestaurantDetailFragment : BaseFragment<FragmentRestaurantDetailBinding>() {

    private val args: RestaurantDetailFragmentArgs by navArgs()

    private val TAG = "RestaurantDetailFragment"

    private val viewModel: RestaurantDetailViewModel by viewModels {
        // Khởi tạo Factory với Repository mới
        val apiService = RetrofitInstance.create(requireContext(), RestaurantApiService::class.java)
        val repository = RestaurantRepository(apiService)
        RestaurantDetailViewModelFactory(repository)

    }

    private val menuAdapter = MenuAdapter { foodItem ->
        //Toast.makeText(context, "Đã chọn: ${foodItem.name}", Toast.LENGTH_SHORT).show()
        navigateToMenuDetail(foodItem.id.toString())
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRestaurantDetailBinding {
        return FragmentRestaurantDetailBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
        binding.rvMenu.adapter = menuAdapter
        viewModel.fetchRestaurantData(args.restaurantId)
    }

    override fun observeData() {
        super.observeData()

        launchOnStarted {
            viewModel.uiState.collect { uiState ->

                uiState.error?.let {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }

                if (uiState.restaurant != null) {
                    Log.d(TAG, "Restaurant: ${uiState.restaurant}")
                    val restaurant = uiState.restaurant
                    binding.tvRestaurantNameDetail.text = restaurant.name
                    binding.tvRestaurantAddressDetail.text = restaurant.address
                    binding.tvRestaurantRatingDetail.text = "N/A"
                    binding.tvRestaurantDistanceDetail.text =
                        restaurant.distance?.let { "${it} km" } ?: ""

                    Glide.with(this@RestaurantDetailFragment)
                        .load(restaurant.imageUrl)
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.loading)
                        .into(binding.ivRestaurantImageBanner)
                }

                if (uiState.menuItems.isNotEmpty()) {
                    Log.d(TAG, "Menu Items: ${uiState.menuItems}")
                    binding.rvMenu.visibility = View.VISIBLE
                    binding.tvErrorMenu.visibility = View.GONE
                    menuAdapter.setData(uiState.menuItems)
                } else {
                    Log.d(TAG, "Menu Items rỗng!")
                    binding.rvMenu.visibility = View.GONE
                    if (!uiState.isLoading && uiState.error == null) {
                        binding.tvErrorMenu.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener { onBack() }
    }

    override fun onBack() {
        super.onBack()
    }

    private fun navigateToMenuDetail(foodItemId: String) {
        val bundle = bundleOf(
            "foodItemId" to foodItemId
        )
        safeNavigate(R.id.action_restaurantDetailFragment_to_restaurantMenuItemFragment, bundle)
    }

}
package com.example.food_order.ui.customer.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.food_order.MainApplication
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.CategoryApiServices
import com.example.food_order.data.api.RestaurantApiService
import com.example.food_order.data.model.common.Category
import com.example.food_order.data.repository.CategoryRepository
import com.example.food_order.data.repository.RestaurantRepository
import com.example.food_order.databinding.FragmentCustomerHomeBinding
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.ui.adapter.CategoryAdapter
import com.example.food_order.ui.adapter.PopularFoodAdapter
import com.example.food_order.ui.adapter.RestaurantAdapter
import com.example.food_order.utils.extension.launchOnStarted
import com.example.food_order.utils.extension.safeNavigate
import com.example.food_order.utils.extension.showToast


class CustomerHomeFragment : BaseFragment<FragmentCustomerHomeBinding>() {

    private val TAG = "CustomerHomeFragment"

    private val viewModel: CustomerHomeViewModel by viewModels {
        val application = requireContext().applicationContext as MainApplication
        val sessionManager = application.sessionManager

        val categoryApiService =
            RetrofitInstance.create(application, CategoryApiServices::class.java)
        val restaurantApiService =
            RetrofitInstance.create(application, RestaurantApiService::class.java)

        val categoryRepo = CategoryRepository(categoryApiService)
        val restaurantRepo = RestaurantRepository(restaurantApiService)

        CustomerHomeViewModelFactory(
            categoryRepo,
            restaurantRepo,
            restaurantApiService,
            sessionManager
        )
    }

    private val categoryAdapter = CategoryAdapter { category ->
        navigateToCategoryDetail(category)
    }
    private val restaurantAdapter = RestaurantAdapter { restaurant ->
        navigateToRestaurantDetail(restaurant.id)
        Log.d(TAG, "Restaurant ID: ${restaurant.id}")
    }
    private val popularFoodAdapter = PopularFoodAdapter { menuItem ->
        // Lấy restaurantId từ ViewModel
        viewModel.restaurantId.value.let { restaurantId ->
            navigateToFoodItemDetail(menuItem.id.toString(), restaurantId)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCustomerHomeBinding {
        return FragmentCustomerHomeBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
        setupAdapter()
    }

    override fun observeData() {
        super.observeData()

        launchOnStarted {
            viewModel.uiState.collect { uiState ->
                // [THÊM] Hiển thị lỗi nếu có
                uiState.error?.let { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
                if (!uiState.isLoading) {
                    categoryAdapter.setData(uiState.categories)
                    restaurantAdapter.setData(uiState.restaurants)
                    popularFoodAdapter.setData(uiState.popularItems)
                }
            }
        }
    }


    override fun initListener() {
        super.initListener()

        binding.edSearch.setOnClickListener { showToast("Chức năng này hiện chưa sử dụng được") }
    }

    override fun onBack() {
        super.onBack()
    }

    private fun setupAdapter() {
        binding.rvCategories.adapter = categoryAdapter
        binding.rvRestaurants.adapter = restaurantAdapter
        binding.rvPopularItems.adapter = popularFoodAdapter
    }

    private fun navigateToCategoryDetail(category: Category) {

        val bundle = bundleOf(
            "categoryId" to category.id,
            "categoryName" to category.name
        )

        safeNavigate(R.id.action_customerHomeFragment_to_categoryDetailFragment, bundle)
    }

    private fun navigateToRestaurantDetail(restaurantId: String) {
        val bundle = bundleOf(
            "restaurantId" to restaurantId
        )
        safeNavigate(R.id.action_customerHomeFragment_to_restaurantDetailFragment, bundle)
    }

    private fun navigateToFoodItemDetail(foodItemId: String, restaurantId: String) {
        val bundle = bundleOf(
            "foodItemId" to foodItemId,
            "restaurantId" to restaurantId
        )
        safeNavigate(R.id.action_customerHomeFragment_to_popularItemDetailFragment, bundle)
    }


}
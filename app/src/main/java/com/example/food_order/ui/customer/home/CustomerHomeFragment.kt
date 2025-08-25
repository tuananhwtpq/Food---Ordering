package com.example.food_order.ui.customer.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.CategoryApiServices
import com.example.food_order.data.model.common.Category
import com.example.food_order.data.repository.CategoryRepository
import com.example.food_order.databinding.FragmentCustomerHomeBinding
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.ui.adapter.CategoryAdapter
import com.example.food_order.ui.adapter.PopularFoodAdapter
import com.example.food_order.ui.adapter.RestaurantAdapter
import com.example.food_order.utils.extension.launchOnStarted
import com.example.food_order.utils.extension.safeNavigate


class CustomerHomeFragment : BaseFragment<FragmentCustomerHomeBinding>() {

    private val viewModel: CustomerHomeViewModel by viewModels {
        val context = requireContext().applicationContext
        val categoryApiService = RetrofitInstance.create(context, CategoryApiServices::class.java)
        val repository = CategoryRepository(categoryApiService)
        CustomerHomeViewModelFactory(repository)
    }

    private val categoryAdapter = CategoryAdapter { category ->
        navigateToCategoryDetail(category)
    }
    private val restaurantAdapter = RestaurantAdapter { restaurant -> }
    private val popularFoodAdapter = PopularFoodAdapter { foodItem -> }

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


}
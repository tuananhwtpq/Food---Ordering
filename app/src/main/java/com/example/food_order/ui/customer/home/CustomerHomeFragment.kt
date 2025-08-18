package com.example.food_order.ui.customer.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.databinding.FragmentCustomerHomeBinding
import com.example.food_order.ui.adapter.CategoryAdapter
import com.example.food_order.ui.adapter.PopularFoodAdapter
import com.example.food_order.ui.adapter.RestaurantAdapter


class CustomerHomeFragment : BaseFragment<FragmentCustomerHomeBinding>() {

    private val viewModel: CustomerHomeViewModel by viewModels()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var restaurantAdapter: RestaurantAdapter
    private lateinit var popularFoodAdapter: PopularFoodAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCustomerHomeBinding {
        return FragmentCustomerHomeBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
        viewModel.loadAllData()
    }

    override fun observeData() {
        super.observeData()

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter = CategoryAdapter(categories)
            binding.rvCategories.adapter = categoryAdapter
        }

        viewModel.restaurants.observe(viewLifecycleOwner) { restaurants ->
            restaurantAdapter = RestaurantAdapter(restaurants)
            binding.rvRestaurants.adapter = restaurantAdapter
        }

        viewModel.popularItems.observe(viewLifecycleOwner) { popularItems ->
            popularFoodAdapter = PopularFoodAdapter(popularItems)
            binding.rvPopularItems.adapter = popularFoodAdapter
        }

    }

    override fun initListener() {
        super.initListener()
    }

    override fun onBack() {
        super.onBack()
    }


}
package com.example.food_order.ui.customer.home.detail.restaurantDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.databinding.FragmentRestaurantDetailBinding

class RestaurantDetailFragment : BaseFragment<FragmentRestaurantDetailBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRestaurantDetailBinding {
        return FragmentRestaurantDetailBinding.inflate(inflater, container, false)
    }

}
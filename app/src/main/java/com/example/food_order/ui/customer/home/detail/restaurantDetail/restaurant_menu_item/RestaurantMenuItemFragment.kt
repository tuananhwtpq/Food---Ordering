package com.example.food_order.ui.customer.home.detail.restaurantDetail.restaurant_menu_item

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.RestaurantApiService
import com.example.food_order.data.repository.RestaurantRepository
import com.example.food_order.databinding.FragmentRestaurantMenuItemBinding
import com.example.food_order.di.RetrofitInstance

class RestaurantMenuItemFragment : BaseFragment<FragmentRestaurantMenuItemBinding>() {

    private val args: RestaurantMenuItemFragmentArgs by navArgs()

    private val viewModel: RestaurantMenuItemViewModel by viewModels {
        val apiService = RetrofitInstance.create(requireContext(), RestaurantApiService::class.java)
        val repository = RestaurantRepository(apiService)
        RestaurantMenuItemViewModelFactory(repository)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRestaurantMenuItemBinding {
        return FragmentRestaurantMenuItemBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()

        val foodItemId = args.foodItemId
        if (foodItemId.isNotEmpty()) {
            viewModel.getMenuItemDetails(foodItemId)
        }
    }

    override fun observeData() {
        super.observeData()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
        }

        viewModel.menuItemDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess { menuItem ->
                binding.tvFoodName.text = menuItem.name
                binding.tvDescription.text = menuItem.description
                binding.tvPrice.text = "$${menuItem.price}"

                Glide.with(this)
                    .load(menuItem.imageUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .into(binding.imgFood)
            }.onFailure { exception ->
                Toast.makeText(context, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
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

}
package com.example.food_order.ui.customer.home.detail.categoryDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.CategoryApiServices
import com.example.food_order.data.repository.CategoryRepository
import com.example.food_order.databinding.FragmentCategoryDetailBinding
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.ui.adapter.RestaurantListAdapter
import com.example.food_order.utils.extension.launchOnStarted
import com.example.food_order.utils.extension.showToast

class CategoryDetailFragment : BaseFragment<FragmentCategoryDetailBinding>() {

    private val args: CategoryDetailFragmentArgs by navArgs()

    private val viewModel: CategoryDetailViewModel by viewModels {
        val context = requireContext().applicationContext
        val apiService = RetrofitInstance.create(context, CategoryApiServices::class.java)
        val repository = CategoryRepository(apiService)
        CategoryDetailViewModelFactory(repository)
    }

    private val restaurantAdapter = RestaurantListAdapter { restaurant ->
        showToast("Bạn đang chọn nhà hàng: ${restaurant.name}")
    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCategoryDetailBinding {
        return FragmentCategoryDetailBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()

        binding.rvRestaurants.adapter = restaurantAdapter
        val categoryId = args.categoryId
        val categoryName = args.categoryName

        binding.toolbar.title = categoryName
        viewModel.fetchRestaurantsByCategoryId(categoryId)
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

    override fun onBack() {
        super.onBack()
    }

}
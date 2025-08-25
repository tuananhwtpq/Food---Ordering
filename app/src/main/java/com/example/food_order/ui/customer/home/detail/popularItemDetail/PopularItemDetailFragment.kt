package com.example.food_order.ui.customer.home.detail.popularItemDetail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.databinding.FragmentPopularItemDetailBinding

class PopularItemDetailFragment : BaseFragment<FragmentPopularItemDetailBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPopularItemDetailBinding {
        return FragmentPopularItemDetailBinding.inflate(inflater, container, false)
    }

}
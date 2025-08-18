package com.example.food_order.ui.customer.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.databinding.FragmentCustomerHomeBinding


class CustomerHomeFragment : BaseFragment<FragmentCustomerHomeBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCustomerHomeBinding {
        return FragmentCustomerHomeBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
    }

    override fun observeData() {
        super.observeData()
    }

    override fun initListener() {
        super.initListener()
    }

    override fun onBack() {
        super.onBack()
    }


}
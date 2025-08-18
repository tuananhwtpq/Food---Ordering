package com.example.food_order.ui.auth.role_selection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.databinding.FragmentRoleSelectionBinding
import com.example.food_order.utils.extension.safeNavigate


class RoleSelectionFragment : BaseFragment<FragmentRoleSelectionBinding>() {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRoleSelectionBinding {
        return FragmentRoleSelectionBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
    }

    override fun observeData() {
        super.observeData()
    }

    override fun initListener() {
        super.initListener()

        binding.btnCustomer.setOnClickListener { handleRoleClicked("customer") }
        binding.btnOwner.setOnClickListener { handleRoleClicked("owner") }

    }

    private fun handleRoleClicked(role: String) {
        val bundle = Bundle().apply {
            putString("userRole", role)
        }
        safeNavigate(R.id.action_roleSelectionFragment_to_loginFragment2, bundle)
    }

    override fun onBack() {
        super.onBack()
    }


}
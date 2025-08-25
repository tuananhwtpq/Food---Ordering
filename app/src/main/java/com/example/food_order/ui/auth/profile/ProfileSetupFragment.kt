package com.example.food_order.ui.auth.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.food_order.MainApplication
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.AddressApiService
import com.example.food_order.data.repository.AddressRepository
import com.example.food_order.databinding.FragmentProfileSetupBinding
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.manager.SessionManager
import com.example.food_order.ui.main.MainActivity


class ProfileSetupFragment : BaseFragment<FragmentProfileSetupBinding>() {

    private val viewModel: ProfileSetupViewModel by viewModels {
        val apiService = RetrofitInstance.create(requireContext(), AddressApiService::class.java)
        val repository = AddressRepository(apiService)
        ProfileSetupViewModelFactory(repository)
    }

    private lateinit var sessionManager: SessionManager

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileSetupBinding {
        return FragmentProfileSetupBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()

        sessionManager = (requireActivity().application as MainApplication).sessionManager

        binding.etFullName.setText(sessionManager.fetchUserName())
    }

    override fun observeData() {
        super.observeData()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        viewModel.saveStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(context, "Lưu thông tin thành công!", Toast.LENGTH_SHORT).show()
                navigateToMain()
            }.onFailure {
                Toast.makeText(context, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun initListener() {
        super.initListener()

        binding.btnSave.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()

            if (address.isEmpty()) {
                Toast.makeText(context, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveAddress(
                addressLine1 = address,
                city = "Hanoi",
                state = "Hanoi",
                zipCode = "100000",
                country = "Vietnam"
            )
        }
    }

    override fun onBack() {
        super.onBack()
    }

    private fun navigateToMain() {
        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("USER_ROLE", sessionManager.fetchUserRole())
        }
        startActivity(intent)
        requireActivity().finish()
    }

}
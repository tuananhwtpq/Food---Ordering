package com.example.food_order.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.databinding.FragmentLoginBinding
import com.example.food_order.ui.main.MainActivity
import com.example.food_order.utils.extension.safeNavigate
import com.example.food_order.utils.extension.showToast
import kotlin.toString


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private var userRole: String? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()

        arguments?.let {
            userRole = it.getString("userRole")
        }

        showToast("userRole: $userRole")

    }

    override fun observeData() {
        super.observeData()
    }

    override fun initListener() {
        super.initListener()

        binding.signupText.setOnClickListener {

            val bundle = Bundle().apply {
                putString("userRole", userRole)
            }

            safeNavigate(R.id.action_loginFragment_to_signupFragment, bundle)
        }

        binding.loginButton.setOnClickListener {
            handleLoginClicked()
        }
    }

    override fun onBack() {
        super.onBack()
    }

    private fun handleLoginClicked() {
        binding.errorTxt.isVisible = false
        val email = binding.loginEmail.text.toString().trim()
        val password = binding.loginPassword.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            showToast("Vui lòng nhập đầy đủ thông tin")
            return
        }

        showToast("Đăng nhập thành công!")

        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("USER_ROLE", userRole)
        }
        startActivity(intent)

    }


}
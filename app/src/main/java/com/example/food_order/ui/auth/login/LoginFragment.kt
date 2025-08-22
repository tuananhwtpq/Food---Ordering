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


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()

    }

    override fun observeData() {
        super.observeData()
    }

    override fun initListener() {
        super.initListener()

        binding.signupText.setOnClickListener {

            safeNavigate(R.id.action_loginFragment_to_signupFragment)
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

        val selectedRole = when (binding.radioGroupRole.checkedRadioButtonId) {
            R.id.radioButtonOwner -> "owner"
            else -> "customer"
        }

        if (email == "admin" && password == "admin" && selectedRole == "owner") {
            navigateToMain("owner")

        } else if (email == "customer" && password == "123456" && selectedRole == "customer") {
            navigateToMain("customer")

        } else {
            binding.errorTxt.text = "Email, mật khẩu hoặc vai trò không đúng"
            binding.errorTxt.isVisible = true
        }
    }

    private fun navigateToMain(userRole: String) {
        showToast("Đăng nhập thành công với vai trò: $userRole")
        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            putExtra("USER_ROLE", userRole)
        }
        startActivity(intent)
        requireActivity().finish()
    }


}
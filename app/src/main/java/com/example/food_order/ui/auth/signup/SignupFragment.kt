package com.example.food_order.ui.auth.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.databinding.FragmentSignupBinding
import com.example.food_order.utils.extension.showToast
import com.google.gson.Gson
import kotlin.toString


class SignupFragment : BaseFragment<FragmentSignupBinding>() {

    private var userRole: String? = null

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(inflater, container, false)
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
            onBack()
        }

        //Xử lý sau
        binding.signupButton.setOnClickListener {
            clickSignupButton()
        }
    }

    override fun onBack() {
        super.onBack()
    }

    private fun clickSignupButton() {
        val email = binding.edEnterEmail.text.toString()
        val password = binding.edPassword.text.toString()
        val username = binding.edUserName.text.toString()
        val reEnterPw = binding.edReEnterpw.text.toString()
        val checkbox = binding.checkboxSignUp.isChecked

//        if (!viewModel.validateLoginInput(username, email, password, reEnterPw, checkbox)) {
//            return
//        }
//        if (!binding.checkboxSignUp.isChecked) {
//            showToast("Bạn cần đồng ý với điều khoản!")
//            return
//        }
//
//        //Sau sẽ cập nhật ở Profile sau
//        val registerRequest = RegisterRequest(
//            username = username,
//            email = email,
//            password = password,
//            firstName = "first_name",
//            lastName = "last_name",
//            dob = "2024-01-01",
//            gender = "MALE"
//        )
//        Log.d("REGISTER_DATA", Gson().toJson(registerRequest))
//        viewModel.register(registerRequest)
    }


}
package com.example.food_order.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.food_order.MainApplication
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.model.request.SignupRequest
import com.example.food_order.databinding.FragmentSignupBinding
import com.example.food_order.ui.main.MainActivity
import com.example.food_order.utils.extension.launchOnStarted
import com.example.food_order.utils.extension.safeNavigate
import com.example.food_order.utils.extension.showToast
import com.example.food_order.utils.state.SignupUiState
import com.google.gson.Gson
import kotlin.toString


class SignupFragment : BaseFragment<FragmentSignupBinding>() {

    private val viewModel: SignupViewModel by viewModels {
        val application = requireActivity().application as MainApplication
        SignupViewModelFactory(
            application.authRepository,
            application.sessionManager
        )
    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSignupBinding {
        return FragmentSignupBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()


    }

    override fun observeData() {
        super.observeData()

        launchOnStarted {
            viewModel.signupUiState.collect { state ->

                when (state) {
                    is SignupUiState.Success -> {
                        navigateToLogin()
                    }

                    is SignupUiState.Error -> {
                        showToast(state.message)
                    }

                    else -> Unit
                }
            }

        }

        launchOnStarted {
            viewModel.error.collect { errorMessage ->
                errorMessage?.let {
                    showToast("Lỗi: $it")
                    Log.d("Lỗi", it)
                    viewModel.clearError()
                }
            }
        }

    }

    override fun initListener() {
        super.initListener()

        binding.signupText.setOnClickListener {
            onBack()
        }

        //Xử lý sau
        binding.signupButton.setOnClickListener {
            handleSignupClicked()
        }
    }

    override fun onBack() {
        super.onBack()
    }

    private fun handleSignupClicked() {
        val name = binding.edUserName.text.toString().trim()
        val email = binding.edEnterEmail.text.toString().trim()
        val password = binding.edPassword.text.toString().trim()
        val rePassword = binding.edReEnterpw.text.toString().trim()
        val checkbox = binding.checkboxSignUp.isChecked

        if (!viewModel.validateLoginInput(name, email, password, rePassword, checkbox)) {
            return
        }


        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showToast("Vui lòng điền đầy đủ thông tin")
            return
        }
        if (password != rePassword) {
            showToast("Mật khẩu không khớp")
            return
        }
        if (!binding.checkboxSignUp.isChecked) {
            showToast("Bạn cần đồng ý với điều khoản")
            return
        }

        val selectedRole = when (binding.radioGroupRole.checkedRadioButtonId) {
            R.id.radioButtonOwner -> "owner"
            else -> "customer"
        }

        val request = SignupRequest(name, email, password, selectedRole)
        viewModel.signupUser(request)
    }

//    private fun navigateToMain(userRole: String) {
//        showToast("Đăng ký thành công với vai trò: $userRole")
//        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            putExtra("USER_ROLE", userRole)
//        }
//        startActivity(intent)
//    }

    private fun navigateToProfileSetup() {
        showToast("Đăng ký thành công! Vui lòng hoàn tất hồ sơ của bạn.")
        safeNavigate(R.id.action_signupFragment2_to_profileSetupFragment)
    }

    private fun navigateToLogin() {
        showToast("Đăng ký thành công! Vui lòng đăng nhập lại.")
        findNavController().popBackStack()
    }


}
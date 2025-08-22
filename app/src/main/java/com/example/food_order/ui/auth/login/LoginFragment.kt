package com.example.food_order.ui.auth.login

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.food_order.MainApplication
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.model.request.LoginRequest
import com.example.food_order.databinding.FragmentLoginBinding
import com.example.food_order.ui.main.MainActivity
import com.example.food_order.utils.extension.launchOnStarted
import com.example.food_order.utils.extension.safeNavigate
import com.example.food_order.utils.extension.showToast
import com.example.food_order.utils.state.LoginUiState
import kotlin.toString


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels {
        val application = requireActivity().application as MainApplication
        LoginViewModelFactory(
            application.authRepository,
            application.sessionManager
        )
    }

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
        launchOnStarted {
            viewModel.loginUiState.collect { state ->
                binding.loadingView.isVisible = state is LoginUiState.Loading

                when (state) {
                    is LoginUiState.Success -> {
                        navigateToMain(state.authResponse.role)
                    }

                    is LoginUiState.Error -> {
                        binding.passwordLayout.error = state.message
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun initListener() {
        super.initListener()

        binding.signupText.setOnClickListener {
            safeNavigate(R.id.action_loginFragment2_to_signupFragment2)
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
            return
        }

        val selectedRole = when (binding.radioGroupRole.checkedRadioButtonId) {
            R.id.radioButtonOwner -> "owner"
            else -> "customer"
        }

        val request = LoginRequest(email, password, selectedRole)
        viewModel.loginUser(request)
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
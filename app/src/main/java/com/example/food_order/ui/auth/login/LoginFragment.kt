package com.example.food_order.ui.auth.login

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.food_order.MainApplication
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.RestaurantApiService
import com.example.food_order.data.model.request.LoginRequest
import com.example.food_order.data.model.response.OwnerRestaurant
import com.example.food_order.databinding.FragmentLoginBinding
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.manager.SessionManager
import com.example.food_order.ui.main.MainActivity
import com.example.food_order.utils.extension.launchOnStarted
import com.example.food_order.utils.extension.safeNavigate
import com.example.food_order.utils.extension.showToast
import com.example.food_order.utils.state.LoginUiState
import kotlinx.coroutines.launch
import kotlin.toString


class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel: LoginViewModel by viewModels {
        val application = requireActivity().application as MainApplication
        LoginViewModelFactory(
            application.authRepository,
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
                        val role = state.authResponse.role
                        if (role.equals("owner", ignoreCase = true)) {
                            onLoginSuccessOwnerFlow(role)
                        } else {
                            navigateToMain(role)
                        }
                    }

                    is LoginUiState.Error -> {
                        //binding.passwordLayout.error = state.message
                        binding.errorTxt.isVisible = true
                        binding.errorTxt.text = state.message
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
    private fun onLoginSuccessOwnerFlow(role: String) {
        val ctx = requireContext()
        val session = SessionManager(ctx)

        // Tạo service có JWT sẵn (đã được RetrofitInstance + AuthInterceptor của bạn xử lý)
        val restaurantApi = RetrofitInstance
            .createAuthorizedServiceGeneric<RestaurantApiService>(ctx)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val res = restaurantApi.getMyRestaurants()
                if (res.isSuccessful) {
                    val list = res.body()?.data.orEmpty()
                    session.saveSelectedRestaurantId(list.first().id)
                    Log.e("OwnerLogin", "getMyRestaurants failed: HTTP ${res.code()}, body=${res.errorBody()?.string()}")

                    when {
                        list.isEmpty() -> {
                            // Không có nhà hàng nào -> xoá selection để tránh null về sau
                            session.clearSelectedRestaurantId()
                            showToast("Tài khoản owner chưa có nhà hàng nào.")
                            navigateToMain(role)
                        }
                        list.size == 1 -> {
                            session.saveSelectedRestaurantId(list.first().id)
                            navigateToMain(role)
                        }
                        else -> {
                            // Có nhiều nhà hàng -> cho chọn
                            showRestaurantPicker(list) { chosen ->
                                session.saveSelectedRestaurantId(chosen.id)
                                navigateToMain(role)
                            }
                        }
                    }
                } else {
                    showToast("Không lấy được danh sách nhà hàng (HTTP ${res.code()}). Tiếp tục vào app.")
                    navigateToMain(role)
                }
            } catch (e: Exception) {
                showToast("Lỗi lấy nhà hàng: ${e.message}")
                navigateToMain(role)
            }
        }
    }

    private fun showRestaurantPicker(
        restaurants: List<OwnerRestaurant>,
        onPicked: (OwnerRestaurant) -> Unit
    ) {
        val names = restaurants.map { it.name }.toTypedArray()
        AlertDialog.Builder(requireContext())
            .setTitle("Chọn nhà hàng")
            .setItems(names) { dlg, idx ->
                onPicked(restaurants[idx])
                dlg.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }



}
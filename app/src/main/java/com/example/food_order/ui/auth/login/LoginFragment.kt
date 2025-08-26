package com.example.food_order.ui.auth.login

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.food_order.MainApplication

import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.OwnerRestaurant
import com.example.food_order.data.api.RestaurantApiService
import com.example.food_order.data.model.owner.RestaurantSelectionBus
import com.example.food_order.data.model.request.LoginRequest
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
                        val token = state.authResponse.token
                        SessionManager(requireContext()).saveAuthDetails(token, role)

                        val isProfileComplete = state.authResponse.isProfileComplete

                        if (isProfileComplete && role.equals("Customer",ignoreCase = true)) {
                            navigateToMain(state.authResponse.role)
                            if (role.equals("owner", ignoreCase = true)) {
                                onLoginSuccessOwnerFlow(role)
                            } else {
                                navigateToMain(role)
                            }
                        } else {
                            if (role.equals("Owner", ignoreCase = true)) {
                                onLoginSuccessOwnerFlow(role)
                            }
                            else {
                                navigateToProfileSetup()
                            }
                        }
                    }

                    is LoginUiState.Error -> {
                        binding.loadingView.isVisible = false
                        // binding.errorTxt.isVisible = false // Bạn có thể muốn hiển thị errorTxt với msg
                        val emailInput = binding.loginEmail.text.toString().trim() // Lấy text từ EditText và trim()
                        val rawErrorMessage = state.message?.trim().orEmpty()

                        val specificErrorMessage = when {
                            rawErrorMessage.contains("401", ignoreCase = true) || rawErrorMessage.contains("unauthorized", ignoreCase = true) ->
                                "Email hoặc mật khẩu không đúng."
                            rawErrorMessage.contains("timeout", ignoreCase = true) ||
                                    rawErrorMessage.contains("failed to connect", ignoreCase = true) ||
                                    rawErrorMessage.contains("unable to resolve host", ignoreCase = true) ->
                                "Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại kết nối mạng."
                            rawErrorMessage.contains("404", ignoreCase = true) -> // Lỗi 404 Not Found
                                "Tài khoản không tồn tại."
                            rawErrorMessage.contains("400", ignoreCase = true) -> // Lỗi 400 Bad Request (có thể do nhiều nguyên nhân)
                                "Yêu cầu không hợp lệ. Vui lòng thử lại." // Hoặc "Sai tài khoản hoặc mật khẩu" nếu API của bạn trả về 400 cho trường hợp đó
                            rawErrorMessage.isBlank() -> // Nếu không có thông điệp lỗi cụ thể từ server
                                "Đăng nhập thất bại. Vui lòng thử lại."
                            // Có thể thêm các trường hợp lỗi cụ thể khác từ API của bạn ở đây
                            rawErrorMessage.contains("BEGIN_OBJECT but was", ignoreCase = true) -> "Sai role!"
                            else -> rawErrorMessage // Nếu không khớp với các trường hợp trên, hiển thị thông báo gốc từ server
                        }

                        // Kiểm tra định dạng email riêng biệt sau khi xử lý các lỗi server
                        // Điều này hợp lý hơn là trộn lẫn với các mã lỗi HTTP
                        var finalMessage = specificErrorMessage
                        if (!isValidEmail(emailInput)) { // Sử dụng một hàm riêng để kiểm tra email
                            finalMessage = "Địa chỉ email không hợp lệ. Vui lòng kiểm tra lại."
                            binding.errorTxt.text = finalMessage // Hiển thị lỗi cụ thể cho email nếu có errorTxt
                            binding.errorTxt.isVisible = true
                        } else if (specificErrorMessage == rawErrorMessage && rawErrorMessage.isNotBlank()) {
                            // Nếu không có lỗi server cụ thể được ánh xạ và có thông báo gốc
                            binding.errorTxt.text = finalMessage
                            binding.errorTxt.isVisible = true
                        } else if (specificErrorMessage != rawErrorMessage) { // Nếu có lỗi server cụ thể được ánh xạ
                            binding.errorTxt.text = finalMessage
                            binding.errorTxt.isVisible = true
                        }


                        showToast(finalMessage)
                        binding.loginEmail.setText("")
                        binding.loginPassword.setText("") // Tùy chọn: Xóa mật khẩu khi đăng nhập sai
                    }

                    else -> Unit
                }
            }
        }
    }
    fun isValidEmail(email: String): Boolean {
        val e = email.contains("@gmail.com",ignoreCase = true) || email.contains("@fpt.edu.vn",ignoreCase = true) ||
                email.contains("@example.com",ignoreCase = true)
        return e
    }
    override fun initListener() {
        super.initListener()

        binding.signupText.setOnClickListener {
            safeNavigate(R.id.action_loginFragment2_to_signupFragment2)
        }

        binding.loginButton.setOnClickListener {
            handleLoginClicked()
        }
        binding.forgetPasswordTxt.setOnClickListener{
            Toast.makeText(this.context,"Chức năng này tạm thời đang bị khóa!",Toast.LENGTH_SHORT).show()
        }


    }

    override fun onBack() {
        super.onBack()
    }

    private fun handleLoginClicked() {
        //binding.errorTxt.isVisible = false
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
        val service = RetrofitInstance.createAuthorizedServiceGeneric<RestaurantApiService>(ctx)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val res = service.getMyRestaurants()
                if (!res.isSuccessful) {
                    Log.e("OwnerLogin", "getMyRestaurants HTTP=${res.code()} body=${res.errorBody()?.string()}")
                    showToast("Không lấy được danh sách nhà hàng (HTTP ${res.code()})")
                    navigateToMain(role); return@launch
                }
                val list = res.body()?.data.orEmpty()
                Log.d("OwnerLogin", "myRestaurants size=${list.size}")
                when {
                    list.isEmpty() -> {
                        session.clearSelectedRestaurantId()
                        showToast("Tài khoản owner chưa có nhà hàng.")
                        navigateToMain(role)
                    }
                    list.size == 1 -> {
                        val r = list.first()
                        session.saveSelectedRestaurantId(r.id, r.name)
                        RestaurantSelectionBus.update(r.id) // <--- thêm dòng này
                        navigateToMain(role)
                    }
                    else -> {
                        val names = list.map { it.name }.toTypedArray()
                        AlertDialog.Builder(ctx)
                            .setTitle("Chọn nhà hàng")
                            .setItems(names) { d, idx ->
                                val chosen = list[idx]
                                session.saveSelectedRestaurantId(chosen.id, chosen.name)
                                RestaurantSelectionBus.update(chosen.id) // <--- thêm dòng này
                                navigateToMain(role)
                                d.dismiss()
                            }
                            .setNegativeButton(android.R.string.cancel) { _, _ -> navigateToMain(role) }
                            .show()
                    }
                }
            } catch (e: Exception) {
                Log.e("OwnerLogin", "getMyRestaurants error", e)
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


    private fun navigateToProfileSetup() {
        showToast("Vui lòng hoàn tất hồ sơ của bạn")
        safeNavigate(R.id.action_loginFragment2_to_profileSetupFragment)
    }


}
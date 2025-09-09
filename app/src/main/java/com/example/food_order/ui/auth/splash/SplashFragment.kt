package com.example.food_order.ui.auth.splash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.food_order.MainApplication
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.databinding.FragmentSplashBinding
import com.example.food_order.manager.SessionManager
import com.example.food_order.ui.auth.AuthActivity
import com.example.food_order.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private lateinit var sessionManager: SessionManager

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()

        sessionManager = (requireActivity().application as MainApplication).sessionManager

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1500L)
            checkLoginStatus()
        }
    }

    private fun checkLoginStatus() {
        val token = sessionManager.fetchAuthToken()
        val role = sessionManager.fetchUserRole()

        if (token.isNullOrEmpty() || role.isNullOrEmpty()) {
            (requireActivity() as? AuthActivity)?.navigateToLogin()

        } else {
            navigateToMain(role)
        }
    }

    private fun navigateToMain(userRole: String) {
        val intent = Intent(requireActivity(), MainActivity::class.java).apply {
            putExtra("USER_ROLE", userRole)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
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
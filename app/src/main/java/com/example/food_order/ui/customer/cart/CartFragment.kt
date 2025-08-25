package com.example.food_order.ui.customer.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.CartApiService
import com.example.food_order.data.repository.CartRepository
import com.example.food_order.databinding.FragmentCartBinding
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.ui.adapter.CartAdapter
import java.text.NumberFormat
import java.util.Locale


class CartFragment : BaseFragment<FragmentCartBinding>() {

    private val viewModel: CartViewModel by viewModels {
        val apiService = RetrofitInstance.create(requireContext(), CartApiService::class.java)
        val repository = CartRepository(apiService)
        CartViewModelFactory(repository)
    }

    private lateinit var cartAdapter: CartAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCartBinding {
        return FragmentCartBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
        setupRecyclerView()
    }

    override fun observeData() {
        super.observeData()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // binding.progressBar.isVisible = isLoading // Giả sử có progress bar
        }

        viewModel.cartResponse.observe(viewLifecycleOwner) { result ->
            result.onSuccess { cartResponse ->
                cartAdapter.setData(cartResponse.items)

                val checkout = cartResponse.checkoutDetails
                val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))

                binding.tvSubtotal.text = formatter.format(checkout.subtotal)
                binding.tvDeliveryFee.text = formatter.format(checkout.deliveryFee)
                binding.tvTotal.text = formatter.format(checkout.total)

            }.onFailure {
                Toast.makeText(context, "Lỗi tải giỏ hàng: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        viewModel.itemUpdateStatus.observe(viewLifecycleOwner) { result ->
            result.onFailure {
                Toast.makeText(context, "Lỗi cập nhật giỏ hàng: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    }

    override fun initListener() {
        super.initListener()

    }

    override fun onBack() {
        super.onBack()
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onQuantityChanged = { cartItemId, newQuantity ->
                viewModel.updateCartItemQuantity(cartItemId, newQuantity)
            },
            onRemoveItem = { cartItemId ->
                viewModel.removeCartItem(cartItemId)
            }
        )
        binding.rvCartItems.adapter = cartAdapter
    }


}
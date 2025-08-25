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
import com.example.food_order.data.api.AddressApiService
import com.example.food_order.data.api.CartApiService
import com.example.food_order.data.api.OrderApiService
import com.example.food_order.data.repository.AddressRepository
import com.example.food_order.data.repository.CartRepository
import com.example.food_order.data.repository.OrderRepository
import com.example.food_order.databinding.FragmentCartBinding
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.ui.adapter.CartAdapter
import java.text.NumberFormat
import java.util.Locale


class CartFragment : BaseFragment<FragmentCartBinding>() {

    private val viewModel: CartViewModel by viewModels {
        val cartApiService = RetrofitInstance.create(requireContext(), CartApiService::class.java)
        val addressApiService =
            RetrofitInstance.create(requireContext(), AddressApiService::class.java)
        val orderApiService = RetrofitInstance.create(requireContext(), OrderApiService::class.java)

        val cartRepo = CartRepository(cartApiService)
        val addressRepo = AddressRepository(addressApiService)
        val orderRepo = OrderRepository(orderApiService)

        CartViewModelFactory(cartRepo, addressRepo, orderRepo)
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

        viewModel.placeOrderStatus.observe(viewLifecycleOwner) { result ->
            result?.let {
                it.onSuccess { response ->
                    Toast.makeText(
                        context,
                        "Đặt hàng thành công! Mã đơn hàng của bạn là #${response.id.take(8)}",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.onPlaceOrderShown()
                }.onFailure {
                    Toast.makeText(context, "Lỗi đặt hàng: ${it.message}", Toast.LENGTH_LONG).show()
                    viewModel.onPlaceOrderShown()
                }
            }
        }

    }

    override fun initListener() {
        super.initListener()

        binding.btnCheckout.setOnClickListener { viewModel.placeOrder() }

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

    override fun onResume() {
        super.onResume()
        viewModel.getCartDetails()
    }


}
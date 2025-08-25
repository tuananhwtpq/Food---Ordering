package com.example.food_order.ui.customer.orders.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.OrderApiService
import com.example.food_order.data.model.common.Order
import com.example.food_order.data.repository.OrderRepository
import com.example.food_order.databinding.FragmentOrderDetailBinding
import com.example.food_order.di.RetrofitInstance
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale


class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>() {

    private val args: OrderDetailFragmentArgs by navArgs()

    private val viewModel: OrderDetailViewModel by viewModels {
        val apiService = RetrofitInstance.create(requireContext(), OrderApiService::class.java)
        val repository = OrderRepository(apiService)
        OrderDetailViewModelFactory(
            repository,
            args.orderId
        )
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderDetailBinding {
        return FragmentOrderDetailBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
        binding.toolbar.title = "Chi tiết đơn hàng"
    }

    override fun observeData() {
        super.observeData()
        viewModel.order.observe(viewLifecycleOwner) { result ->
            result.onSuccess { order ->
                displayOrderDetails(order)
                binding.tvError.visibility = View.GONE
            }
            result.onFailure { exception ->
                binding.tvError.text = exception.message
                binding.tvError.visibility = View.VISIBLE
                binding.contentLayout.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.contentLayout.visibility = if (isLoading) View.GONE else View.VISIBLE
        }
    }

    private fun displayOrderDetails(order: Order) {
        binding.tvRestaurantName.text = order.restaurant?.name ?: "Không rõ nhà hàng"
        binding.tvOrderStatus.text = "Trạng thái: ${order.status}"
        binding.tvPaymentStatus.text = "Thanh toán: ${order.paymentStatus}"

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        try {
            val date = parser.parse(order.createdAt)
            binding.tvOrderDate.text = "Ngày đặt: ${formatter.format(date)}"
        } catch (e: Exception) {
            binding.tvOrderDate.text = "Ngày đặt: ${order.createdAt.take(10)}"
        }

        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        binding.tvOrderTotal.text = "Tổng cộng: ${currencyFormatter.format(order.totalAmount)}"

        binding.tvOrderItems.text = order.items.joinToString("\n") {
            "${it.menuItemName} x${it.quantity}"
        }

        binding.tvAddress.text = order.address?.let {
            "${it.addressLine1}, ${it.city}, ${it.country}"
        } ?: "Không có thông tin địa chỉ"
    }

    override fun initListener() {
        super.initListener()
        binding.toolbar.setNavigationOnClickListener {
            onBack()
        }
    }

    override fun onBack() {
        findNavController().navigateUp()
    }
}
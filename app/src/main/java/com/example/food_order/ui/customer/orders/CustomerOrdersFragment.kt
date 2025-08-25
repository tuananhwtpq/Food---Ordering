package com.example.food_order.ui.customer.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.OrderApiService
import com.example.food_order.data.repository.OrderRepository
import com.example.food_order.databinding.FragmentCustomerOrdersBinding
import com.example.food_order.di.RetrofitInstance
import com.example.food_order.ui.adapter.OrderHistoryAdapter

class CustomerOrdersFragment : BaseFragment<FragmentCustomerOrdersBinding>() {

    private val viewModel: OrderHistoryViewModel by viewModels {
        val apiService = RetrofitInstance.create(requireContext(), OrderApiService::class.java)
        val repository = OrderRepository(apiService)
        OrderHistoryViewModelFactory(repository)
    }

    private lateinit var orderAdapter: OrderHistoryAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCustomerOrdersBinding {
        return FragmentCustomerOrdersBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
        setupRecyclerView()
    }

    override fun observeData() {
        super.observeData()

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }

        viewModel.orders.observe(viewLifecycleOwner) { result ->
            result.onSuccess { orders ->
                orderAdapter.setData(orders.sortedByDescending { it.createdAt })
            }.onFailure {
                Toast.makeText(
                    context,
                    "Lỗi tải lịch sử đơn hàng: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
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
        orderAdapter = OrderHistoryAdapter { order ->
            Toast.makeText(
                context,
                "Xem chi tiết đơn hàng: ${order.id.take(8)}",
                Toast.LENGTH_SHORT
            ).show()
            // findNavController().navigate(...)
        }
        binding.rvOrders.adapter = orderAdapter
    }


}
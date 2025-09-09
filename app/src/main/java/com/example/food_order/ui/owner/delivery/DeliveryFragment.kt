package com.example.food_order.ui.owner.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_order.data.repository.OrderListSource
import com.example.food_order.data.repository.OrderListSource.OrderStatus
import com.example.food_order.databinding.FragmentDeliveryBinding
import com.example.food_order.ui.orders.OrdersSharedViewModel
import com.example.food_order.ui.owner.adapter.ListDeliveryAdapter
import com.example.food_order.utils.extension.showToast

class DeliveryFragment : Fragment() {

    private var _binding: FragmentDeliveryBinding? = null
    private val binding get() = _binding!!

    private val ordersVM: OrdersSharedViewModel by activityViewModels()
    private lateinit var adapter: ListDeliveryAdapter
    private var currentFilter: OrderStatus? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupFilterClicks()
        ordersVM.delivery.observe(viewLifecycleOwner) { applyFilterAndShow(it) }
    }

    private fun setupRecycler() {
        adapter = ListDeliveryAdapter(mutableListOf())
        binding.recyclerViewDelirey.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDelirey.adapter = adapter
        binding.recyclerViewDelirey.setHasFixedSize(true)
    }

    private fun setupFilterClicks() = with(binding) {
        tvAccepted.setOnClickListener {
            currentFilter = OrderStatus.ACCEPTED
            showToast("Accepted")
            ordersVM.delivery.value?.let { applyFilterAndShow(it) }
        }
        tvDelived.setOnClickListener {
            currentFilter = OrderStatus.DELIVERED
            showToast("Delivered")
            ordersVM.delivery.value?.let { applyFilterAndShow(it) }
        }
        tvDeliveryFailed.setOnClickListener {
            currentFilter = OrderStatus.DELIVERY_FAILED
            showToast("Delivery Failed")
            ordersVM.delivery.value?.let { applyFilterAndShow(it) }
        }
        tvRejected.setOnClickListener {
            currentFilter = OrderStatus.REJECTED
            showToast("Delivery Rejected")
            ordersVM.delivery.value?.let { applyFilterAndShow(it) }
        }
        // bấm vào tiêu đề để xóa filter
        tvDelivery.setOnClickListener {
            currentFilter = null
            showToast("All")
            ordersVM.delivery.value?.let { applyFilterAndShow(it) }
        }
    }

    private fun applyFilterAndShow(all: List<OrderListSource.OrderSimple>) {
        val filtered = currentFilter?.let { st -> all.filter { it.status == st } } ?: all
        adapter.submitList(filtered)
        binding.emptyView.isVisible = filtered.isEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

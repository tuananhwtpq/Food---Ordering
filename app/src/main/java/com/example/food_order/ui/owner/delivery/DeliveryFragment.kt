package com.example.food_order.ui.owner.delivery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_order.data.demo.OrderDemoStore
import com.example.food_order.data.repository.OrderListSource.OrderSimple
import com.example.food_order.data.repository.OrderListSource.OrderStatus
import com.example.food_order.databinding.FragmentDeliveryBinding

class DeliveryFragment : Fragment() {

    private var _binding: FragmentDeliveryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ListDeliveryAdapter

    // Bộ lọc hiện tại
    private var currentFilter: (OrderSimple) -> Boolean = { it.status in IN_PROGRESS }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeliveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListDeliveryAdapter()
        binding.recyclerViewDelirey.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewDelirey.adapter = adapter

        // Lắng nghe dữ liệu demo chung
        OrderDemoStore.orders.observe(viewLifecycleOwner, Observer { list ->
            applyFilter(list ?: emptyList())
        })

        // Click 4 nút filter ở card trên cùng
        binding.tvAccepted.setOnClickListener {
            setFilter(IN_PROGRESS)
            setSelected(binding.tvAccepted)
        }
        binding.tvRejected.setOnClickListener {
            setFilter(setOf(OrderStatus.REJECTED, OrderStatus.CANCELLED))
            setSelected(binding.tvRejected)
        }
        binding.tvDelived.setOnClickListener {
            setFilter(setOf(OrderStatus.DELIVERED))
            setSelected(binding.tvDelived)
        }
        binding.tvDeliveryFailed.setOnClickListener {
            setFilter(setOf(OrderStatus.DELIVERY_FAILED))
            setSelected(binding.tvDeliveryFailed)
        }

        // mặc định: In-progress
        setSelected(binding.tvAccepted)
    }

    private fun setFilter(statuses: Set<OrderStatus>) {
        currentFilter = { it.status in statuses }
        OrderDemoStore.orders.value?.let { applyFilter(it) }
    }

    private fun applyFilter(list: List<OrderSimple>) {
        adapter.submitList(list.filter(currentFilter))
    }

    private fun setSelected(selected: View) {
        // tô trạng thái chọn (tuỳ ý, ở đây chỉ set isSelected để bạn style bằng selector)
        listOf(binding.tvAccepted, binding.tvRejected, binding.tvDelived, binding.tvDeliveryFailed)
            .forEach { it.isSelected = (it.id == selected.id) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Nhóm "đang xử lý/đang giao"
        private val IN_PROGRESS = setOf(
            OrderStatus.ACCEPTED,
            OrderStatus.PREPARING,
            OrderStatus.READY,
            OrderStatus.ASSIGNED,
            OrderStatus.OUT_FOR_DELIVERY
        )
    }
}

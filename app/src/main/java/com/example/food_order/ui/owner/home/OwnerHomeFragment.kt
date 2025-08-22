package com.example.food_order.ui.owner.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_order.data.demo.OrderDemoStore
import com.example.food_order.data.repository.OrderListSource.OrderStatus
import com.example.food_order.databinding.FragmentOwnerHomeBinding

class OwnerHomeFragment : Fragment() {

    private var _binding: FragmentOwnerHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: OwnerOrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOwnerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OwnerOrderAdapter(
            onAccept = {
                OrderDemoStore.accept(it.id)
                Toast.makeText(requireContext(), "Đã chấp nhận đơn #${it.id}", Toast.LENGTH_SHORT).show()
            },
            onReject = {
                OrderDemoStore.reject(it.id)
                Toast.makeText(requireContext(), "Đã từ chối đơn #${it.id}", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvListOrderPending.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListOrderPending.adapter = adapter

        // Chỉ show đơn đang chờ duyệt trên Home
        OrderDemoStore.orders.observe(viewLifecycleOwner, Observer { list ->
            val pending = (list ?: mutableListOf()).filter { it.status == OrderStatus.PENDING_ACCEPTANCE }
            adapter.submitList(pending)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

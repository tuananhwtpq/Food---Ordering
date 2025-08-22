package com.example.food_order.ui.owner.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_order.databinding.FragmentOwnerHomeBinding
import com.example.food_order.ui.orders.OrdersSharedViewModel
import com.example.food_order.R
import com.example.food_order.ui.owner.adapter.OwnerOrdersAdapter

class OwnerHomeFragment : Fragment() {

    private var _binding: FragmentOwnerHomeBinding? = null
    private val binding get() = _binding!!

    private val ordersVM: OrdersSharedViewModel by activityViewModels()
    private lateinit var adapter: OwnerOrdersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOwnerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()

        ordersVM.pending.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvListOrderPending.text = "Danh sách đơn hàng chờ: (${list.size})"
        }
    }

    private fun setupRecycler() {
        adapter = OwnerOrdersAdapter(
            data = mutableListOf(),
            onAccept = { ordersVM.accept(it) },
            onReject = { ordersVM.reject(it) }
        )
        binding.rvListOrderPending.layoutManager = LinearLayoutManager(requireContext())
        binding.rvListOrderPending.adapter = adapter
        binding.rvListOrderPending.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

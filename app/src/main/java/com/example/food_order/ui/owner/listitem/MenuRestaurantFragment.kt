// MenuRestaurantFragment.kt
package com.example.food_order.ui.owner.listitem

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_order.R

import com.google.android.material.snackbar.Snackbar
import androidx.recyclerview.widget.RecyclerView
import com.example.food_order.ui.owner.adapter.MenuAdapter
import com.example_food_order.data.repository.MenuItem

class MenuRestaurantFragment : Fragment(R.layout.fragment_menu_restaurant) {

    private val restaurantId: String by lazy {
        // Lấy từ arguments; bắt buộc phải truyền từ màn trước
        requireArguments().getString("restaurantId")
            ?: throw IllegalArgumentException("Thiếu restaurantId để tải menu")
    }

    private val viewModel by viewModels<MenuRestaurantViewModel> {
        MenuRestaurantViewModel.provideFactory(requireContext(), restaurantId)
    }
    private lateinit var adapter: MenuAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvMenu)
        adapter = MenuAdapter(onItemClick = ::onItemClick)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // Quan sát dữ liệu
        viewModel.items.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            msg?.let { Snackbar.make(view, it, Snackbar.LENGTH_LONG).show() }
        }

        // Gọi API
        viewModel.loadMenu()

        // (nếu có FAB thêm món, setOnClickListener ở đây và mở BottomSheet như bạn đã làm)
    }

    private fun onItemClick(item: MenuItem) {
        // Mở bottom sheet để xem/sửa
        val sheet = MenuItemBottomSheet.newInstance(item)
        sheet.onSave = {
            val id = it.id
            if (id == null) {
                // tạo mới
                viewModel.create(
                    body = com.example.food_order.data.model.request.MenuRequest(
                        name = it.name,
                        description = it.description,
                        price = it.price,
                        imageUrl = it.imageUrl,
                        restaurantId = it.restaurantId
                    )
                )
            } else {
                // cập nhật
                viewModel.update(
                    itemId = id,
                    body = com.example.food_order.data.model.request.MenuRequest(
                        name = it.name,
                        description = it.description,
                        price = it.price,
                        imageUrl = it.imageUrl,
                        restaurantId = it.restaurantId
                    )
                )
            }
        }
        sheet.onDelete = { id ->
            viewModel.delete(id)
        }
        sheet.show(childFragmentManager, "menu_item_details")
    }
}

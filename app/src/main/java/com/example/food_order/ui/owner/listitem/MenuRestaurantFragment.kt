// MenuRestaurantFragment.kt (bản rút gọn đã sửa)
package com.example.food_order.ui.owner.listitem

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_order.R
import com.example.food_order.manager.SessionManager
import com.example.food_order.ui.owner.adapter.MenuAdapter
import com.example_food_order.data.repository.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.example.food_order.utils.extension.showToast

class MenuRestaurantFragment : Fragment(R.layout.fragment_menu_restaurant) {

    private lateinit var viewModel: MenuRestaurantViewModel

    private lateinit var adapter: MenuAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 1) Lấy id từ args -> nếu rỗng thì lấy từ Session
        val resolvedId = arguments?.getString("restaurantId")?.takeIf { it.isNotBlank() }
            ?: SessionManager(requireContext()).fetchSelectedRestaurantId()

// 2) Log để bạn nhìn ở Logcat tag = MenuVM
        Log.d("MenuVM", "resolvedId=$resolvedId")

// 3) Nếu chưa có id -> chặn lại để tránh loadMenu với id rỗng
        if (resolvedId.isNullOrBlank()) {
            showToast("Bạn chưa chọn nhà hàng. Hãy đăng nhập Owner và chọn 1 nhà hàng.")
            return
        }

// 4) Tạo ViewModel bằng factory với id hợp lệ
        val factory = MenuRestaurantViewModel.provideFactory(requireContext(), resolvedId)
        viewModel = ViewModelProvider(this, factory)[MenuRestaurantViewModel::class.java]

        super.onViewCreated(view, savedInstanceState)

        val rv = view.findViewById<RecyclerView>(R.id.rvMenu)
        adapter = MenuAdapter(onItemClick = ::onItemClick)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) { list ->
            adapter.submit(list) // hoặc adapter.submitList(list) nếu adapter bạn có hàm đó
        }
        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
        }
        val restaurantId = arguments?.getString("restaurantId")
        Log.d("MenuVM", "restaurantId=$restaurantId")
        viewModel.loadMenu()
    }

    private fun onItemClick(item: MenuItem) {
        val sheet = MenuItemBottomSheet.newInstance(item)
        sheet.onSave = {
            val req = com.example.food_order.data.model.request.MenuRequest(
                name = it.name,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = it.restaurantId
            )
            if (it.id == null) viewModel.create(req) else viewModel.update(it.id, req)
        }
        sheet.onDelete = { id -> viewModel.delete(id) }
        sheet.show(childFragmentManager, "menu_item_details")
    }
}

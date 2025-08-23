package com.example.food_order.ui.owner.listitem

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_order.R
import com.example.food_order.data.model.request.MenuRequest
import com.example.food_order.data.repository.MenuItem
import com.example.food_order.manager.SessionManager
import com.example.food_order.ui.owner.adapter.MenuAdapter
import com.example.food_order.utils.extension.showToast
import com.google.android.material.snackbar.Snackbar

class MenuRestaurantFragment : Fragment(R.layout.fragment_menu_restaurant) {

    private lateinit var viewModel: MenuRestaurantViewModel
    private lateinit var adapter: MenuAdapter

    // giữ bản đầy đủ để filter cục bộ
    private var fullList: List<MenuItem> = emptyList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1) Resolve restaurantId
        val resolvedId = arguments?.getString("restaurantId")?.takeIf { it.isNotBlank() }
            ?: SessionManager(requireContext()).fetchSelectedRestaurantId()

        Log.d("MenuVM", "resolvedId=$resolvedId")
        if (resolvedId.isNullOrBlank()) {
            showToast("Bạn chưa chọn nhà hàng. Hãy đăng nhập Owner và chọn 1 nhà hàng.")
            return
        }

        // 2) ViewModel
        val factory = MenuRestaurantViewModel.provideFactory(requireContext(), resolvedId)
        viewModel = ViewModelProvider(this, factory)[MenuRestaurantViewModel::class.java]

        // 3) RecyclerView + Adapter
        val rv = view.findViewById<RecyclerView>(R.id.rvMenu)
        adapter = MenuAdapter(onItemClick = ::onItemClick)
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        // 4) Search EditText (LẤY BÊN TRONG onViewCreated)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)

        // Observe danh sách -> lưu fullList rồi áp filter theo ô đang gõ
        viewModel.items.observe(viewLifecycleOwner) { list ->
            fullList = list
            val q = etSearch.text?.toString().orEmpty()
            adapter.submit(filter(fullList, q))
        }

        viewModel.error.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrBlank()) Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show()
        }

        // 5) Nút tạo mới
        view.findViewById<View>(R.id.fabAdd)?.setOnClickListener {
            val sheet = MenuItemBottomSheet.newForCreate(restaurantId = viewModelRestaurantId())
            sheet.onCreate = { req ->
                viewModel.create(req) { sheet.dismiss() }
            }
            sheet.show(childFragmentManager, "menu_item_create")
        }

        // 6) Search: gõ tới đâu lọc tới đó
        etSearch.doAfterTextChanged { s ->
            val q = s?.toString().orEmpty()
            adapter.submit(filter(fullList, q))
        }

        // 7) Enter/Search -> ẩn bàn phím
        etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            val isIme = actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE  ||
                    actionId == EditorInfo.IME_ACTION_GO
            val isEnter = event != null &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    event.keyCode == KeyEvent.KEYCODE_ENTER
            if (isIme || isEnter) {
                hideKeyboard(etSearch)
                true
            } else false
        })

        // 8) Load data
        viewModel.loadMenu()
    }

    private fun viewModelRestaurantId(): String {
        return arguments?.getString("restaurantId")
            ?: SessionManager(requireContext()).fetchSelectedRestaurantId()
            ?: ""
    }

    private fun filter(list: List<MenuItem>, q: String): List<MenuItem> {
        if (q.isBlank()) return list
        return list.filter { item ->
            item.name.contains(q, ignoreCase = true) ||
                    (item.description?.contains(q, ignoreCase = true) == true)
        }
    }

    private fun hideKeyboard(v: View) {
        val imm = requireContext()
            .getSystemService(android.content.Context.INPUT_METHOD_SERVICE)
                as android.view.inputmethod.InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    private fun onItemClick(item: MenuItem) {
        val sheet = MenuItemBottomSheet.newInstance(item)
        sheet.onSave = { updated ->
            val req = MenuRequest(
                name = updated.name,
                description = updated.description,
                price = updated.price,
                imageUrl = updated.imageUrl,
                restaurantId = updated.restaurantId,
                arModelUrl = null
            )
            if (updated.id == null) {
                viewModel.create(req)
            } else {
                viewModel.update(updated.id, req)
            }
        }
        sheet.onDelete = { id -> viewModel.delete(id) }
        sheet.show(childFragmentManager, "menu_item_details")
    }
}

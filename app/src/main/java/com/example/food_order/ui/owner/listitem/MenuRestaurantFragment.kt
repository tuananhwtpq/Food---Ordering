package com.example.food_order.ui.owner.listitem

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_order.R
import com.example.food_order.data.repository.FakeMenuRepository
import com.example.food_order.data.repository.MenuItem
import com.example.food_order.ui.owner.adapter.MenuAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class MenuRestaurantFragment : Fragment(R.layout.fragment_menu_restaurant) {

    companion object {
        private const val ARG_RESTAURANT_ID = "restaurant_id"

        fun newInstance(restaurantId: String) = MenuRestaurantFragment().apply {
            arguments = bundleOf(ARG_RESTAURANT_ID to restaurantId)
        }
    }

    private lateinit var rv: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var adapter: MenuAdapter

    private val repo by lazy { FakeMenuRepository.MenuRepositoryProvider.instance() }
    private val restaurantId: String by lazy {
        arguments?.getString(ARG_RESTAURANT_ID) ?: "r1"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv = view.findViewById(R.id.rvMenu)
        fab = view.findViewById(R.id.fabAdd)

        adapter = MenuAdapter(
            onItemClick = { item ->
                openBottomSheet(item)
            }
        )

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = adapter

        fab.setOnClickListener { openBottomSheet(newBlankItem()) }

        loadData()
    }

    private fun newBlankItem() = MenuItem(
        id = null,
        restaurantId = restaurantId,
        name = "",
        description = null,
        price = 0.0,
        imageUrl = null,
        arModelUrl = null,
        createdAt = null
    )

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val items = withContext(Dispatchers.IO) {
                repo.getMenu(restaurantId)
            }
            // LƯU Ý: adapter của bạn là submit(...), KHÔNG phải submitList
            adapter.submit(items)
        }
    }

    private fun openBottomSheet(item: MenuItem) {
        val sheet = MenuItemBottomSheet.newInstance(item).apply {
            onSave = { updated ->
                viewLifecycleOwner.lifecycleScope.launch {
                    withContext(Dispatchers.IO) { repo.update(updated) }
                    loadData()
                    dismissAllowingStateLoss()
                }
            }
            onDelete = { id ->
                viewLifecycleOwner.lifecycleScope.launch {
                    withContext(Dispatchers.IO) { repo.delete(id) }
                    loadData()
                    dismissAllowingStateLoss()
                }
            }
        }
        sheet.show(childFragmentManager, "menu_item_details")
    }
}

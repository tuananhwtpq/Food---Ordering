package com.example.food_order.ui.owner.listitem

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.food_order.R
import com.example.food_order.data.repository.MenuItem
import com.example.food_order.data.repository.MenuRepository
import com.example.food_order.data.repository.FakeMenuRepository.MenuRepositoryProvider
import com.example.food_order.databinding.FragmentMenuRestaurantBinding
import com.example.food_order.ui.owner.adapter.MenuAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch

class MenuRestaurantFragment : Fragment(R.layout.fragment_menu_restaurant) {

    private var _binding: FragmentMenuRestaurantBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MenuAdapter
    private lateinit var repo: MenuRepository

    private var fullList: List<MenuItem> = emptyList()

    private val restaurantId: String by lazy {
        arguments?.getString(ARG_RESTAURANT_ID) ?: "r1"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMenuRestaurantBinding.bind(view)

        // Repo: mặc định Fake, có thể chuyển sang API ở Application qua MenuRepositoryProvider.useApi(...)
        repo = MenuRepositoryProvider.instance()

        // RecyclerView + Adapter
        adapter = MenuAdapter(onItemClick = ::showItemDetailsBottomSheet)
        binding.rvMenu.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMenu.adapter = adapter

        // Search
        binding.etSearch.doAfterTextChanged { text ->
            filter(text?.toString().orEmpty())
        }

        // Tải dữ liệu
        loadMenu()
    }

    private fun loadMenu() {
        binding.progress.visibility = View.VISIBLE
        binding.tvEmpty.visibility = View.GONE
        binding.rvMenu.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                fullList = repo.getMenu(restaurantId)
                adapter.submit(fullList)
                toggleEmpty()
            } catch (e: Exception) {
                binding.tvEmpty.text = getString(R.string.error_loading_data)
                binding.tvEmpty.visibility = View.VISIBLE
                binding.rvMenu.visibility = View.GONE
            } finally {
                binding.progress.visibility = View.GONE
            }
        }
    }

    private fun filter(query: String) {
        if (query.isBlank()) {
            adapter.submit(fullList)
        } else {
            val q = query.lowercase()
            adapter.submit(
                fullList.filter {
                    it.name.lowercase().contains(q) ||
                            (it.description ?: "").lowercase().contains(q)
                }
            )
        }
        toggleEmpty()
    }

    private fun toggleEmpty() {
        val empty = adapter.itemCount == 0
        binding.tvEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        binding.rvMenu.visibility = if (empty) View.GONE else View.VISIBLE
    }

    // ========= BottomSheet chi tiết + Sửa / Lưu / Xoá =========
    private fun showItemDetailsBottomSheet(item: MenuItem) {
        val dialog = BottomSheetDialog(requireContext())
        val sheetView = layoutInflater.inflate(R.layout.fragment_bottomsheet_menu_item_details, null)
        dialog.setContentView(sheetView)

        val btnClose = sheetView.findViewById<ImageButton>(R.id.btnClose)
        val ivImage = sheetView.findViewById<ImageView>(R.id.ivImageLarge)
        val etName = sheetView.findViewById<EditText>(R.id.etName)
        val etPrice = sheetView.findViewById<EditText>(R.id.etPrice)
        val etDescription = sheetView.findViewById<EditText>(R.id.etDescription)
        val etImageUrl = sheetView.findViewById<EditText>(R.id.etImageUrl)
        val etIdReadOnly = sheetView.findViewById<EditText>(R.id.etIdReadOnly)
        val etRestaurantId = sheetView.findViewById<EditText>(R.id.etRestaurantIdReadOnly)
        val etCreatedAt = sheetView.findViewById<EditText>(R.id.etCreatedAtReadOnly)
        val btnEdit = sheetView.findViewById<View>(R.id.btnEdit)
        val btnSave = sheetView.findViewById<View>(R.id.btnSave)
        val btnDelete = sheetView.findViewById<View>(R.id.btnDelete)

        // Bind data
        Glide.with(sheetView.context).load(item.imageUrl).into(ivImage)
        etName.setText(item.name)
        etPrice.setText(item.price.toString())
        etDescription.setText(item.description ?: "")
        etImageUrl.setText(item.imageUrl ?: "")
        etIdReadOnly.setText(item.id ?: "")
        etRestaurantId.setText(item.restaurantId)
        etCreatedAt.setText(item.createdAt ?: "")

        // Mặc định: không cho sửa → các EditText đã disabled sẵn trong XML

        btnEdit.setOnClickListener {
            setEditable(true, etName, etPrice, etDescription, etImageUrl)
            btnSave.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {
            // Gom dữ liệu sửa
            val updated = item.copy(
                // Không đổi id, restaurantId, createdAt
                name = etName.text?.toString()?.trim().orEmpty(),
                description = etDescription.text?.toString()?.trim()?.ifBlank { null },
                price = etPrice.text?.toString()?.toDoubleOrNull() ?: item.price,
                imageUrl = etImageUrl.text?.toString()?.trim()?.ifBlank { null }
            )

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    repo.update(updated)
                    Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show()
                    setEditable(false, etName, etPrice, etDescription, etImageUrl)
                    btnSave.visibility = View.GONE
                    // reload list để phản ánh thay đổi
                    loadMenu()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), getString(R.string.error_saving), Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnDelete.setOnClickListener {
            val id = item.id
            if (id.isNullOrBlank()) {
                Toast.makeText(requireContext(), "Không thể xoá món chưa có id", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    repo.delete(id)
                    Toast.makeText(requireContext(), getString(R.string.deleted), Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                    loadMenu()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), getString(R.string.error_deleting), Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnClose.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun setEditable(enable: Boolean, vararg ets: EditText) {
        ets.forEach { it.isEnabled = enable }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_RESTAURANT_ID = "restaurantId"
        fun newInstance(restaurantId: String) = MenuRestaurantFragment().apply {
            arguments = Bundle().apply { putString(ARG_RESTAURANT_ID, restaurantId) }
        }
    }
}

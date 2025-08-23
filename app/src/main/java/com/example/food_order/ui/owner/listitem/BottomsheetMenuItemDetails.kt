package com.example.food_order.ui.owner.listitem

import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.food_order.R
import com.example.food_order.ui.owner.adapter.MenuAdapter
import com.example.food_order.data.repository.MenuItem

class MenuItemBottomSheet : BottomSheetDialogFragment(R.layout.fragment_bottomsheet_menu_item_details) {

    // callbacks ra Fragment
    var onCreate: ((com.example.food_order.data.model.request.MenuRequest) -> Unit)? = null
    var onSave: ((MenuItem) -> Unit)? = null
    var onDelete: ((String) -> Unit)? = null

    companion object {
        private const val ARG_ID = "id"
        private const val ARG_RES_ID = "resId"
        private const val ARG_NAME = "name"
        private const val ARG_DESC = "desc"
        private const val ARG_PRICE = "price"
        private const val ARG_IMAGE = "image"
        private const val ARG_CREATED = "created"

        fun newInstance(item: MenuItem) = MenuItemBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_ID, item.id)
                putString(ARG_RES_ID, item.restaurantId)
                putString(ARG_NAME, item.name)
                putString(ARG_DESC, item.description)
                putDouble(ARG_PRICE, item.price)
                putString(ARG_IMAGE, item.imageUrl)
                putString(ARG_CREATED, item.createdAt)
            }
        }

        fun newForCreate(restaurantId: String): MenuItemBottomSheet = MenuItemBottomSheet().apply {
            arguments = Bundle().apply {
                putString(ARG_ID, null) // null -> create mode
                putString(ARG_RES_ID, restaurantId)
                putString(ARG_NAME, "")
                putString(ARG_DESC, "")
                putString(ARG_IMAGE, "")
                putDouble(ARG_PRICE, 0.0)
                putString(ARG_CREATED, null)
            }
        }
    }

    // Views
    private lateinit var btnClose: ImageButton
    private lateinit var btnEdit: Button
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDescription: EditText
    private lateinit var etImageUrl: EditText

    private lateinit var etIdReadOnly: EditText
    private lateinit var etRestaurantIdReadOnly: EditText
    private lateinit var etCreatedAtReadOnly: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // find views
        btnClose = view.findViewById(R.id.btnClose)
        btnEdit  = view.findViewById(R.id.btnEdit)
        btnSave  = view.findViewById(R.id.btnSave)
        btnDelete= view.findViewById(R.id.btnDelete)

        etName = view.findViewById(R.id.etName)
        etPrice = view.findViewById(R.id.etPrice)
        etDescription = view.findViewById(R.id.etDescription)
        etImageUrl = view.findViewById(R.id.etImageUrl)

        etIdReadOnly = view.findViewById(R.id.etIdReadOnly)
        etRestaurantIdReadOnly = view.findViewById(R.id.etRestaurantIdReadOnly)
        etCreatedAtReadOnly = view.findViewById(R.id.etCreatedAtReadOnly)

        // beautify a bit (không đổi XML để tránh conflict)
        listOf(btnEdit, btnSave, btnDelete).forEach {
            it.isAllCaps = false
        }

        // read args
        val id = requireArguments().getString(ARG_ID)
        val restaurantId = requireArguments().getString(ARG_RES_ID) ?: ""
        val name = requireArguments().getString(ARG_NAME) ?: ""
        val desc = requireArguments().getString(ARG_DESC)
        val price = requireArguments().getDouble(ARG_PRICE)
        val image = requireArguments().getString(ARG_IMAGE)
        val created = requireArguments().getString(ARG_CREATED)

        // bind
        etName.setText(name)
        etPrice.setText(if (price == 0.0) "" else price.toString())
        etDescription.setText(desc ?: "")
        etImageUrl.setText(image ?: "")

        etIdReadOnly.setText(id ?: "—")
        etRestaurantIdReadOnly.setText(restaurantId)
        etCreatedAtReadOnly.setText(created ?: "—")

        val isCreate = id.isNullOrBlank()

        // trạng thái nút ban đầu
        if (isCreate) {
            // tạo mới -> chỉ Lưu + Xóa; cho phép nhập ngay
            setEditing(true)
            btnEdit.visibility = View.GONE
            btnSave.visibility = View.VISIBLE
            btnDelete.visibility = View.VISIBLE
        } else {
            // xem chi tiết -> hiện Sửa + Xóa; Lưu ẩn
            setEditing(false)
            btnEdit.visibility = View.VISIBLE
            btnSave.visibility = View.GONE
            btnDelete.visibility = View.VISIBLE
        }

        // click Sửa -> bật edit, ẩn Sửa, hiện Lưu
        btnEdit.setOnClickListener {
            setEditing(true)
            btnEdit.visibility = View.GONE
            btnSave.visibility = View.VISIBLE
        }

        // click Lưu:
        btnSave.setOnClickListener {
            val newName = etName.text.toString().trim()
            val newPrice = etPrice.text.toString().toDoubleOrNull() ?: 0.0
            val newDesc = etDescription.text.toString().trim().ifEmpty { null }
            val newImg  = etImageUrl.text.toString().trim().ifEmpty { null }

            if (newName.isBlank() || newPrice <= 0.0) {
                Toast.makeText(requireContext(), "Tên & giá phải hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isCreate) {
                // tạo mới
                val req = com.example.food_order.data.model.request.MenuRequest(
                    restaurantId = restaurantId,
                    name = newName,
                    description = newDesc,
                    price = newPrice,
                    imageUrl = newImg,
                    arModelUrl = null
                )
                onCreate?.invoke(req)
                dismiss() // đóng sheet sau khi gửi callback
            } else {
                // cập nhật
                val updated = MenuItem(
                    id = id,
                    restaurantId = restaurantId,
                    name = newName,
                    description = newDesc,
                    price = newPrice,
                    imageUrl = newImg,
                    arModelUrl = null,
                    createdAt = created
                )
                onSave?.invoke(updated)
                dismiss()
            }
        }

        // click Xóa:
        btnDelete.setOnClickListener {
            if (isCreate) {
                // tạo mới -> coi như "hủy"
                dismiss()
            } else {
                id?.let { onDelete?.invoke(it) }
                dismiss()
            }
        }

        btnClose.setOnClickListener { dismiss() }
    }

    private fun setEditing(editing: Boolean) {
        listOf(etName, etPrice, etDescription, etImageUrl).forEach { it.isEnabled = editing }
        // các ô readonly luôn khóa
        listOf(etIdReadOnly, etRestaurantIdReadOnly, etCreatedAtReadOnly).forEach { it.isEnabled = false }
        // chỉ điều khiển visibility ở nơi gọi; hàm này chỉ lo enable/disable field
    }
}

/** Giữ lại extension submitList cho adapter hiện tại */
fun MenuAdapter.submitList(items: List<MenuItem>) {
    this.submit(items)
}

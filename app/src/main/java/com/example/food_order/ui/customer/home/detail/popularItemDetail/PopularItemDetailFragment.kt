package com.example.food_order.ui.customer.home.detail.popularItemDetail

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.food_order.R
import com.example.food_order.base_view.BaseFragment
import com.example.food_order.data.api.CartApiService
import com.example.food_order.data.api.RestaurantApiService
import com.example.food_order.data.repository.CartRepository
import com.example.food_order.data.repository.RestaurantRepository
import com.example.food_order.databinding.FragmentPopularItemDetailBinding
import com.example.food_order.di.RetrofitInstance
import java.text.NumberFormat
import java.util.Locale

class PopularItemDetailFragment : BaseFragment<FragmentPopularItemDetailBinding>() {

    private val args: PopularItemDetailFragmentArgs by navArgs()
    private val TAG = "PopularItemDetailFragment"

    private val viewModel: PopularItemDetailViewModel by viewModels {
        val restaurantApiService =
            RetrofitInstance.create(requireContext(), RestaurantApiService::class.java)
        val cartApiService = RetrofitInstance.create(requireContext(), CartApiService::class.java)

        val restaurantRepo = RestaurantRepository(restaurantApiService)
        val cartRepo = CartRepository(cartApiService)

        PopularItemDetailViewModelFactory(restaurantRepo, cartRepo)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPopularItemDetailBinding {
        return FragmentPopularItemDetailBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        super.setupView()
        viewModel.getMenuItemDetails(args.foodItemId)
    }

    override fun observeData() {
        super.observeData()

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // binding.progressBar.isVisible = isLoading
        }

        viewModel.menuItemDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess { menuItem ->
                binding.tvFoodName.text = menuItem.name
                binding.tvDescription.text = menuItem.description
                val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                binding.tvPrice.text = formatter.format(menuItem.price)

                Glide.with(this)
                    .load(menuItem.imageUrl)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .into(binding.imgFood)
            }.onFailure { exception ->
                Toast.makeText(context, "Lỗi: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.quantity.observe(viewLifecycleOwner) { quantity ->
            binding.tvQuantity.text = String.format("%02d", quantity)
        }

        viewModel.addToCartStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(context, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show()
                onBack()
            }.onFailure {
                Toast.makeText(context, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.showClearCartDialog.observe(viewLifecycleOwner) { (restaurantId, menuItemId) ->
            showClearCartConfirmationDialog(restaurantId, menuItemId)
        }
    }

    override fun initListener() {
        super.initListener()

        binding.btnBack.setOnClickListener { onBack() }

        binding.btnPlus.setOnClickListener {
            viewModel.incrementQuantity()
        }

        binding.btnMinus.setOnClickListener {
            viewModel.decrementQuantity()
        }

        binding.btnAddCart.setOnClickListener {
            viewModel.onAddToCartClicked(args.restaurantId, args.foodItemId)
        }
    }

    override fun onBack() {
        super.onBack()
    }

    private fun showClearCartConfirmationDialog(restaurantId: String, menuItemId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Bắt đầu giỏ hàng mới?")
            .setMessage("Giỏ hàng của bạn đang có món từ một nhà hàng khác. Bạn có muốn xóa giỏ hàng cũ và thêm món này không?")
            .setPositiveButton("Đồng ý") { dialog, _ ->
                viewModel.clearCartAndAddItem(restaurantId, menuItemId)
                dialog.dismiss()
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}

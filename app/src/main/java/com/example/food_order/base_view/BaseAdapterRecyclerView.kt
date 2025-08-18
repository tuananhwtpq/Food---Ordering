package com.example.food_order.base_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.food_order.utils.extension.clickAnimation

abstract class BaseAdapterRecyclerView<T, VB : ViewBinding>(
    dataList: MutableList<T>? = null
) : RecyclerView.Adapter<BaseViewHolder<VB>>() {

    //viewbindig instance for creating viewholder
    private var binding: VB? = null

    //click listenter for item interaction
    private var onClickItem: ((item: T?, position: Int) -> Unit)? = null
    private var onLongCLickItem: ((item: T?, position: Int) -> Boolean)? = null

    //animation control flags

    private var enableClickAnimation = false
    private var enableLongClickAnimation = false

    // Danh sách dữ liệu của adapter
    //Internal setter để đảm bảo data intergrity

    var dataList: MutableList<T> = dataList ?: arrayListOf()
        internal set

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        val binding = inflateBinding(LayoutInflater.from(parent.context), parent)
        return BaseViewHolder(requireNotNull(binding)).apply {
            bindViewClick(this, viewType)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        bindData(holder.binding, dataList[position], position)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<VB>,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        if (payloads.isEmpty()) {
            //k co payload, su dung binding bth
            onBindViewHolder(holder, position)
        } else {
            // co payload
            bindData(holder.binding, dataList[position], position)
        }

    }

    //Bind data vao Viewbinding
    protected abstract fun bindData(binding: VB, item: T, position: Int)

    protected abstract fun inflateBinding(inflater: LayoutInflater, parent: ViewGroup): VB

    //bind click listener cho viewHolder
    // ccó thể custom click behavior
    open fun bindViewClick(viewHolder: BaseViewHolder<VB>, viewType: Int) {
        //set up normal click
        viewHolder.itemView.setOnClickListener {
            //Đoạn này khác base do k sử dụng đc bindingAdapterPosition
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                //thực hiện animation nếu enable
                if (enableClickAnimation) {
                    it.clickAnimation()
                }
                onClickItem?.invoke(dataList.getOrNull(position), position)
            }

        }
        //setup long lick
        viewHolder.itemView.setOnLongClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {

                if (enableLongClickAnimation) {
                    it.clickAnimation()
                }
                onLongCLickItem?.invoke(dataList.getOrNull(position), position) ?: false
            } else {
                false
            }

        }
    }

    fun setOnClickItem(listener: (item: T?, position: Int) -> Unit){
        this.onClickItem = listener
    }

    //region Data Management Methods

    open fun setData(@IntRange(from = 0) index: Int, data: T){
        if (index < dataList.size){
            dataList[index] = data
            notifyItemChanged(index)
        }
    }

    //Xoa item tai vi tri
    open fun removeAt(@IntRange(from = 0) position: Int){
        if (position < dataList.size){
            dataList.removeAt(position)
            notifyItemChanged(position)
        }
    }

    //Set danh sach data
    @SuppressLint("NotifyDataSetChanged")
    open fun setDataList(data: Collection<T>){
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    //CLear data
    @SuppressLint("NotifyDataSetChanged")
    open fun clearData(){
        dataList.clear()
        notifyDataSetChanged()
    }

    //Them data vao cuoi ds

    open fun addDataList(data: Collection<T>){
        val start = data.size
        dataList.addAll(data)
        notifyItemRangeChanged(start, data.size)
    }

    //endregion


}
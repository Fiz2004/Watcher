package com.faa.watcher.core.presentation.main

import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.faa.watcher.R
import com.faa.watcher.core.presentation.main.model.DishItemUi
import com.faa.watcher.databinding.ItemDishBinding

class DishAdapter(
    private val itemClicked: (DishItemUi) -> Unit,
    private val chkSelectChanged: (DishItemUi) -> Unit,
) : BaseQuickAdapter<DishItemUi, DishAdapter.DishViewHolder>(R.layout.item_dish) {

    override fun convert(holder: DishViewHolder, item: DishItemUi) {
        holder.bind(item)
    }

    override fun convert(holder: DishViewHolder, item: DishItemUi, payloads: List<Any>) {
        holder.bind(item, payloads)
    }

    inner class DishViewHolder(view: View) : BaseViewHolder(view) {
        private var binding: ItemDishBinding? = null

        fun bind(item: DishItemUi, payloads: List<Any> = emptyList()) {
            if (payloads.isEmpty()) {
                binding = ItemDishBinding.bind(itemView).apply {
                    txtName.text = item.name
                    txtPrice.text = item.price.toString()
                    Glide.with(context)
                        .load(item.image)
                        .error(R.drawable.baseline_no_food_24)
                        .into(imgImage)
                    root.setOnClickListener {
                        itemClicked(item)
                    }
                    chkSelect.setOnClickListener {
                        chkSelectChanged(item)
                    }
                }
            }
            binding?.chkSelect?.isChecked = item.isChecked
        }
    }
}
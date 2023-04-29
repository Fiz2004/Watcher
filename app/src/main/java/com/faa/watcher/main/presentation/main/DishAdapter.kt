package com.faa.watcher.main.presentation.main

import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.faa.watcher.R
import com.faa.watcher.main.presentation.model.DishItemUi
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
                    txtPrice.text = context.resources.getQuantityString(
                        R.plurals.currency,
                        item.price,
                        item.price
                    )
                    Glide.with(context)
                        .load(item.image)
                        .placeholder(R.drawable.pic_load)
                        .error(R.drawable.pic_error_load)
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
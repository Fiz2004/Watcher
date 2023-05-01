package com.faa.watcher.main.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.faa.watcher.R
import com.faa.watcher.databinding.ItemDishBinding
import com.faa.watcher.main.presentation.main.model.MainDishesItemUiState

class DishAdapter(
    private val itemClicked: (MainDishesItemUiState) -> Unit,
    private val chkSelectChanged: (MainDishesItemUiState) -> Unit,
) : ListAdapter<MainDishesItemUiState, DishAdapter.DishViewHolder>(MainDishesItemUiState.Comparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = ItemDishBinding.inflate(layoutInflater, parent, false)
        return DishViewHolder(view)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val dayWeek = getItem(position)
        holder.bind(dayWeek)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int, payloads: List<Any>) {
        val dayWeek = getItem(position)
        holder.bind(dayWeek, payloads)
    }

    inner class DishViewHolder(private var binding: ItemDishBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MainDishesItemUiState, payloads: List<Any> = emptyList()) {
            if (payloads.isEmpty()) {
                binding = ItemDishBinding.bind(itemView).apply {
                    txtName.text = item.name
                    txtPrice.text = root.context.resources.getQuantityString(
                        R.plurals.currency,
                        item.price,
                        item.price
                    )
                    Glide.with(root.context)
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
            binding.chkSelect.isChecked = item.isChecked
        }
    }
}
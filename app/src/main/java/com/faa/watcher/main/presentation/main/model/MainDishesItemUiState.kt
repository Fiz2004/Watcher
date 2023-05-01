package com.faa.watcher.main.presentation.main.model

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.faa.watcher.main.domain.model.Dish

data class MainDishesItemUiState(
    val id: String,
    val name: String,
    val image: String?,
    val price: Int,
    val isChecked: Boolean
) {
    companion object Comparator : DiffUtil.ItemCallback<MainDishesItemUiState>() {
        override fun areItemsTheSame(oldItem: MainDishesItemUiState, newItem: MainDishesItemUiState): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MainDishesItemUiState, newItem: MainDishesItemUiState): Boolean {
            return oldItem == newItem
        }

        override fun getChangePayload(oldItem: MainDishesItemUiState, newItem: MainDishesItemUiState): Any? {
            if (oldItem.id == newItem.id) {
                val diff = Bundle()
                if (oldItem.isChecked != newItem.isChecked) {
                    diff.putBoolean("isChecked", true)
                }
                if (!diff.isEmpty)
                    return diff
            }

            return super.getChangePayload(oldItem, newItem)
        }
    }
}

fun Dish.toMainDishItemUiState(): MainDishesItemUiState {
    return MainDishesItemUiState(
        id = id,
        name = name,
        image = image,
        price = price,
        isChecked = false
    )
}
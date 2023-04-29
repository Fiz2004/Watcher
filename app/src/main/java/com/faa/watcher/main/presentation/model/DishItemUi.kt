package com.faa.watcher.main.presentation.model

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.faa.watcher.main.domain.model.Dish

data class DishItemUi(
    val id: String,
    val name: String,
    val description: String,
    val image: String?,
    val price: Int,
    val isChecked: Boolean
) {
    fun toDomain(): Dish {
        return Dish(
            id = id,
            name = name,
            description = description,
            image = image,
            price = price
        )
    }
}

fun Dish.toItemUi(): DishItemUi {
    return DishItemUi(
        id = id,
        name = name,
        description = description,
        image = image,
        price = price,
        isChecked = false
    )
}

object DishItemUiComparator : DiffUtil.ItemCallback<DishItemUi>() {
    override fun areItemsTheSame(oldItem: DishItemUi, newItem: DishItemUi): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DishItemUi, newItem: DishItemUi): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: DishItemUi, newItem: DishItemUi): Any? {
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
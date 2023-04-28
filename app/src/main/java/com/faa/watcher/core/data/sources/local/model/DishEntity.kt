package com.faa.watcher.core.data.sources.local.model

import com.faa.watcher.core.domain.model.Dish

data class DishEntity(
    val id: String,
    val name: String,
    val description: String?,
    val image: String?,
    val price: Int,
) {
    fun toDomain(): Dish {
        return Dish(
            id = id,
            name = name,
            description = description.orEmpty(),
            image = image,
            price = price
        )
    }
}
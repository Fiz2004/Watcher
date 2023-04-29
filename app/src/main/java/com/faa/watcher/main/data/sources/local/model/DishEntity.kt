package com.faa.watcher.main.data.sources.local.model

import com.faa.watcher.main.domain.model.Dish

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

fun Dish.toEntity(): DishEntity {
    return DishEntity(
        id = id,
        name = name,
        description = description,
        image = image,
        price = price
    )
}
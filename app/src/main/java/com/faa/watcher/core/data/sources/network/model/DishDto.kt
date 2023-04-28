package com.faa.watcher.core.data.sources.network.model

import com.faa.watcher.core.data.sources.local.model.DishEntity

data class DishDto(
    val id: String,
    val name: String,
    val description: String?,
    val image: String?,
    val price: Int,
) {
    fun toEntity(): DishEntity {
        return DishEntity(
            id = id,
            name = name,
            description = description,
            image = image,
            price = price
        )
    }
}
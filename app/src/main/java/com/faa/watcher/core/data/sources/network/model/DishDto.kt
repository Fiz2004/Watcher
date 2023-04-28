package com.faa.watcher.core.data.sources.network.model

data class DishDto(
    val id: String,
    val name: String,
    val description: String?,
    val image: String?,
    val price: Int,
)
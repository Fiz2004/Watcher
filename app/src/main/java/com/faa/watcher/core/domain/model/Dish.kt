package com.faa.watcher.core.domain.model

data class Dish(
    val id: String,
    val name: String,
    val description: String,
    val image: String?,
    val price: Int,
)
package com.faa.watcher.main.presentation.detail.model

import com.faa.watcher.main.domain.model.Dish

data class DetailDishesUiState(
    val id: String,
    val name: String,
    val description: String,
    val image: String?,
)

fun Dish.toDetailDishItemUiState(): DetailDishesUiState {
    return DetailDishesUiState(
        id = id,
        name = name,
        description = description,
        image = image,
    )
}
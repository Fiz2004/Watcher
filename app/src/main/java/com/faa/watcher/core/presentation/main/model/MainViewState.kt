package com.faa.watcher.core.presentation.main.model

data class MainViewState(
    val dishes: List<DishItemUi>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
) {
    val isBtnEnabled: Boolean
        get() {
            return dishes?.count { it.isChecked } != 0
        }
}
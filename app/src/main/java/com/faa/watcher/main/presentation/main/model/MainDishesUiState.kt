package com.faa.watcher.main.presentation.main.model

data class MainDishesUiState(
    val dishes: List<MainDishesItemUiState>? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false
) {
    val isContainerMainIsVisible: Boolean
        get() {
            return if (isLoading && dishes == null) {
                false
            } else {
                !isError
            }
        }

    val isTxtNotFoundIsVisible: Boolean
        get() {
            return dishes?.isEmpty() == true
        }

    val isBtnDeleteEnabled: Boolean
        get() {
            return dishes != null && dishes.count { it.isChecked } != 0 && !isLoading && !isError
        }
}
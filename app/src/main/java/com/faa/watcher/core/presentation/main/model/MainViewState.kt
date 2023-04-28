package com.faa.watcher.core.presentation.main.model

import com.faa.watcher.core.presentation.model.DishItemUi

data class MainViewState(
    val dishes: List<DishItemUi>? = null,
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
            return dishes?.count { it.isChecked } != 0 && !isLoading
        }
}
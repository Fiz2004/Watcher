package com.faa.watcher.core.presentation.main.model

sealed class MainEvent {
    object DeleteButtonClicked : MainEvent()
    data class DishClicked(val dish: DishItemUi) : MainEvent()
    data class ChkSelectChanged(val dish: DishItemUi) : MainEvent()
}
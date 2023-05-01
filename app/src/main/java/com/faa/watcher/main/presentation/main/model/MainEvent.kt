package com.faa.watcher.main.presentation.main.model

sealed class MainEvent {
    object DeleteButtonClicked : MainEvent()
    object ReloadData : MainEvent()
    data class DishClicked(val dish: MainDishesItemUiState) : MainEvent()
    data class ChkSelectChanged(val dish: MainDishesItemUiState) : MainEvent()
}
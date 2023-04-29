package com.faa.watcher.main.presentation.main.model

import com.faa.watcher.main.presentation.model.DishItemUi

sealed class MainEvent {
    object DeleteButtonClicked : MainEvent()
    object ReloadData : MainEvent()
    data class DishClicked(val dish: DishItemUi) : MainEvent()
    data class ChkSelectChanged(val dish: DishItemUi) : MainEvent()
}
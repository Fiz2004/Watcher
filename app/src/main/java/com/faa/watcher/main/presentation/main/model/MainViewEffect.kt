package com.faa.watcher.main.presentation.main.model

import com.faa.watcher.common.model.UiText

sealed class MainViewEffect {
    data class ShowMessage(val uiText: UiText) : MainViewEffect()
    data class MoveDetailScreen(val id: String) : MainViewEffect()
}
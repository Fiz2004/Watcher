package com.faa.watcher.core.presentation.main.model

sealed class MainViewEffect {
    data class ShowMessage(val message: UiText) : MainViewEffect()
}
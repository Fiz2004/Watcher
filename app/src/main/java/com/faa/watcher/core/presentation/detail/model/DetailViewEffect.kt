package com.faa.watcher.core.presentation.detail.model

import com.faa.watcher.core.presentation.main.model.UiText

sealed class DetailViewEffect {
    data class ShowMessage(val uiText: UiText) : DetailViewEffect()
}
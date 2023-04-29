package com.faa.watcher.main.presentation.detail.model

import com.faa.watcher.common.model.UiText

sealed class DetailViewEffect {
    data class ShowMessage(val uiText: UiText) : DetailViewEffect()
}
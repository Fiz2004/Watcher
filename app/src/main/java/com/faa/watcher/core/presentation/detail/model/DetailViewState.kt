package com.faa.watcher.core.presentation.detail.model

import com.faa.watcher.core.domain.model.Dish

data class DetailViewState(
    val dish: Dish? = null,
)
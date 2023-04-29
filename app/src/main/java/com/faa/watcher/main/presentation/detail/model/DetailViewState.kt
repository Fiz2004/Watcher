package com.faa.watcher.main.presentation.detail.model

import com.faa.watcher.main.domain.model.Dish

data class DetailViewState(
    val dish: Dish? = null,
)
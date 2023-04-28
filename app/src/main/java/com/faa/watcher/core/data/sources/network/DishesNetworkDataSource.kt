package com.faa.watcher.core.data.sources.network

import com.faa.watcher.core.data.sources.network.model.DishDto

interface DishesNetworkDataSource {
    suspend fun getDishes(): List<DishDto>
}
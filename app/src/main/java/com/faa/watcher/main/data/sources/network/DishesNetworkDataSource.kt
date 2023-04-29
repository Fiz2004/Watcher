package com.faa.watcher.main.data.sources.network

import com.faa.watcher.main.data.sources.network.model.DishDto

interface DishesNetworkDataSource {
    suspend fun getDishes(): Result<List<DishDto>>
}
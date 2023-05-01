package com.faa.watcher.main.data.sources.network

import com.faa.watcher.main.domain.model.Dish

interface DishesNetworkDataSource {
    suspend fun getDishes(): List<Dish>
}
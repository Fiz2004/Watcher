package com.faa.watcher.core.data.sources.local

import com.faa.watcher.core.data.sources.local.model.DishEntity

interface DishesLocalDataSource {
    suspend fun getDishes(): List<DishEntity>
    suspend fun saveDishes(dishes: List<DishEntity>): Boolean
    suspend fun deleteDishes(dishes: List<DishEntity>): Boolean
}
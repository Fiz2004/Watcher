package com.faa.watcher.core.data.sources.local

import com.faa.watcher.core.data.sources.local.model.DishEntity
import kotlinx.coroutines.flow.StateFlow

interface DishesLocalDataSource {
    suspend fun observeDishes(): StateFlow<List<DishEntity>?>
    suspend fun saveDishes(dishes: List<DishEntity>): Result<Unit>
    suspend fun deleteDishes(dishes: List<DishEntity>): Result<Unit>
}
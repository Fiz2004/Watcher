package com.faa.watcher.core.domain.repository

import com.faa.watcher.core.domain.model.Dish
import kotlinx.coroutines.flow.StateFlow

interface DishesRepository {
    fun observeDishes(): StateFlow<Result<List<Dish>?>>

    suspend fun deleteDishes(dishes: List<Dish>): Result<Unit>
}
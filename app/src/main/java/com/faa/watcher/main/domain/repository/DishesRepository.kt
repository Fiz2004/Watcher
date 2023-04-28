package com.faa.watcher.main.domain.repository

import com.faa.watcher.main.domain.model.Dish
import kotlinx.coroutines.flow.StateFlow

interface DishesRepository {
    fun observeDishes(): StateFlow<Result<List<Dish>?>>

    suspend fun deleteDishes(dishesIds: Set<String>): Result<Unit>
    suspend fun getDish(id: String): Result<Dish>
    suspend fun getDishes()
}
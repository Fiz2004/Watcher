package com.faa.watcher.main.data.sources.local

import com.faa.watcher.main.domain.model.Dish
import kotlinx.coroutines.flow.Flow

interface DishesLocalDataSource {
    val dishes: Flow<List<Dish>?>

    suspend fun getDish(id: String): Dish
    suspend fun getDishes(): List<Dish>?
    suspend fun saveDishes(dishes: List<Dish>)
    suspend fun deleteDishes(dishesIds: Set<String>)
}
package com.faa.watcher.main.domain.repository

import com.faa.watcher.main.domain.model.Dish
import kotlinx.coroutines.flow.Flow

interface DishesRepository {
    val dishes: Flow<List<Dish>?>

    suspend fun deleteDishes(dishesIds: Set<String>)
    suspend fun getDish(id: String): Dish
    suspend fun fetchDishes()
}
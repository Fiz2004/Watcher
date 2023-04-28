package com.faa.watcher.core.domain.repository

import com.faa.watcher.core.domain.model.Dish

interface DishesRepository {
    suspend fun getDishes(isNeedRefresh: Boolean): List<Dish>

    suspend fun deleteDishes(dishes: List<Dish>): Boolean
}
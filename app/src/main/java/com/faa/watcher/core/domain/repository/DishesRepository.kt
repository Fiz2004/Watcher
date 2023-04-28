package com.faa.watcher.core.domain.repository

import com.faa.watcher.core.domain.model.Dish

interface DishesRepository {
    suspend fun getDishes(): List<Dish>
}
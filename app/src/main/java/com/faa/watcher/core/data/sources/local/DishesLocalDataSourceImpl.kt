package com.faa.watcher.core.data.sources.local

import com.faa.watcher.core.data.sources.local.model.DishEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishesLocalDataSourceImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) : DishesLocalDataSource {

    private var dishes: List<DishEntity> = listOf()

    override suspend fun getDishes(): List<DishEntity> {
        return dishes
    }

    override suspend fun saveDishes(dishes: List<DishEntity>): Boolean {
        return withContext(dispatcher) {
            this@DishesLocalDataSourceImpl.dishes = dishes
            return@withContext true
        }
    }
}
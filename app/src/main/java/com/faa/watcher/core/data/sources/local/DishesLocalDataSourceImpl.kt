package com.faa.watcher.core.data.sources.local

import com.faa.watcher.core.data.sources.local.model.DishEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishesLocalDataSourceImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) : DishesLocalDataSource {

    private var dishes: MutableStateFlow<List<DishEntity>?> = MutableStateFlow(null)

    override suspend fun observeDishes(): StateFlow<List<DishEntity>?> {
        return dishes.asStateFlow()
    }

    override suspend fun saveDishes(dishes: List<DishEntity>): Result<Unit> {
        return withContext(dispatcher) {
            this@DishesLocalDataSourceImpl.dishes.value = dishes
            Result.success(Unit)
        }
    }

    override suspend fun deleteDishes(dishes: List<DishEntity>): Result<Unit> {
        return withContext(dispatcher) {
            delay(1500)
            this@DishesLocalDataSourceImpl.dishes.value = this@DishesLocalDataSourceImpl.dishes.value
                ?.filter { !dishes.map { it.id }.contains(it.id) }
            Result.success(Unit)
        }
    }
}
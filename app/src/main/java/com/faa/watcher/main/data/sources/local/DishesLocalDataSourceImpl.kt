package com.faa.watcher.main.data.sources.local

import android.content.res.Resources
import com.faa.watcher.di.DispatcherIO
import com.faa.watcher.main.data.sources.local.model.DishEntity
import com.faa.watcher.main.data.sources.local.model.toEntity
import com.faa.watcher.main.domain.model.Dish
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val TIME_DELETE = 1500L

@Singleton
class DishesLocalDataSourceImpl @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher
) : DishesLocalDataSource {

    private val _dishes: MutableStateFlow<List<DishEntity>?> = MutableStateFlow(null)
    override val dishes: Flow<List<Dish>?> = _dishes.map { dishes ->
        dishes?.map { it.toDomain() }
    }

    override suspend fun getDish(id: String): Dish {
        return withContext(dispatcher) {
            val dish = _dishes.value?.find { it.id == id }?.toDomain()
            dish ?: throw Resources.NotFoundException()
        }
    }

    override suspend fun getDishes(): List<Dish>? {
        return withContext(dispatcher) {
            _dishes.value?.map { it.toDomain() }
        }
    }

    override suspend fun saveDishes(dishes: List<Dish>) {
        withContext(dispatcher) {
            _dishes.value = dishes.map { it.toEntity() }
        }
    }

    override suspend fun deleteDishes(dishesIds: Set<String>) {
        withContext(dispatcher) {
            delay(TIME_DELETE)
            _dishes.value = _dishes.value
                ?.filter { dishEntity ->
                    !dishesIds.contains(dishEntity.id)
                }
        }
    }
}
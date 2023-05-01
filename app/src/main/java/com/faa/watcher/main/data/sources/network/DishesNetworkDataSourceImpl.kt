package com.faa.watcher.main.data.sources.network

import com.faa.watcher.di.DispatcherIO
import com.faa.watcher.main.domain.model.Dish
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val AVERAGE_TIME_REQUEST = 2500L

@Singleton
class DishesNetworkDataSourceImpl @Inject constructor(
    @DispatcherIO private val dispatcher: CoroutineDispatcher
) : DishesNetworkDataSource {

    override suspend fun getDishes(): List<Dish> {
        return withContext(dispatcher) {
            delay(AVERAGE_TIME_REQUEST)
            Stubs.dishes.map { it.toDomain() }
        }
    }
}
package com.faa.watcher.core.data.sources.network

import com.faa.watcher.core.data.sources.network.model.DishDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

private const val AVERAGE_TIME_REQUEST = 800L

@Singleton
class DishesNetworkDataSourceImpl @Inject constructor(
    private val dispatcher: CoroutineDispatcher
) : DishesNetworkDataSource {

    override suspend fun getDishes(): List<DishDto> {
        return withContext(dispatcher) {
            delay(AVERAGE_TIME_REQUEST)
            Stubs.dishes
        }
    }
}
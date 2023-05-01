package com.faa.watcher.main.data.repository

import com.faa.watcher.main.data.sources.local.DishesLocalDataSource
import com.faa.watcher.main.data.sources.network.DishesNetworkDataSource
import com.faa.watcher.main.domain.model.Dish
import com.faa.watcher.main.domain.repository.DishesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Singleton
class DishesRepositoryImpl @Inject constructor(
    private val dishesNetworkDataSources: DishesNetworkDataSource,
    private val dishesLocalDataSources: DishesLocalDataSource,
    private val externalScope: CoroutineScope
) : DishesRepository {

    override val dishes: Flow<List<Dish>?> = dishesLocalDataSources.dishes

    override suspend fun getDish(id: String): Dish {
        return dishesLocalDataSources.getDish(id)
    }

    override suspend fun fetchDishes() {
        if (dishesLocalDataSources.getDishes() == null) {
            val dishesNetwork = dishesNetworkDataSources.getDishes()
            dishesLocalDataSources.saveDishes(dishesNetwork)
        }
    }

    override suspend fun deleteDishes(dishesIds: Set<String>) {
        suspendCoroutine {
            externalScope.launch {
                try {
                    dishesLocalDataSources.deleteDishes(dishesIds)
                    it.resumeWith(Result.success(Unit))
                } catch (e: Exception) {
                    it.resumeWithException(e)
                }
            }
        }
    }

}
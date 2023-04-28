package com.faa.watcher.core.data.repository

import com.faa.watcher.core.data.sources.local.DishesLocalDataSource
import com.faa.watcher.core.data.sources.network.DishesNetworkDataSource
import com.faa.watcher.core.domain.model.Dish
import com.faa.watcher.core.domain.repository.DishesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishesRepositoryImpl @Inject constructor(
    private val dishesNetworkDataSources: DishesNetworkDataSource,
    private val dishesLocalDataSources: DishesLocalDataSource
) : DishesRepository {

    override suspend fun getDishes(): List<Dish> {
        val dishes = dishesNetworkDataSources.getDishes()
        dishesLocalDataSources.saveDishes(dishes.map { it.toEntity() })
        return dishesLocalDataSources.getDishes().map { it.toDomain() }
    }

}
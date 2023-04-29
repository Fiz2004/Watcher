package com.faa.watcher.main.data.repository

import com.faa.watcher.main.data.sources.local.DishesLocalDataSource
import com.faa.watcher.main.data.sources.local.model.toEntity
import com.faa.watcher.main.data.sources.network.DishesNetworkDataSource
import com.faa.watcher.main.domain.model.Dish
import com.faa.watcher.main.domain.repository.DishesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishesRepositoryImpl @Inject constructor(
    private val dishesNetworkDataSources: DishesNetworkDataSource,
    private val dishesLocalDataSources: DishesLocalDataSource,
    externalScope: CoroutineScope
) : DishesRepository {

    private var dishes: MutableStateFlow<Result<List<Dish>?>> =
        MutableStateFlow(Result.success(null))

    init {
        externalScope.launch {
            dishesLocalDataSources.observeDishes().collect { dishes ->
                this@DishesRepositoryImpl.dishes.value =
                    Result.success(dishes?.map { it.toDomain() })
            }
        }
    }

    private suspend fun refreshDishes() {
        val dishesNetwork = dishesNetworkDataSources.getDishes()
        dishesNetwork.fold(

            onSuccess = { dishes ->
                dishesLocalDataSources.saveDishes(dishes.map { it.toEntity() })
            },

            onFailure = {
                dishes.value = Result.failure(it)
            }

        )
    }

    override suspend fun getDish(id: String): Result<Dish> {
        return dishes.value.fold(
            onSuccess = { dishes ->
                val dish = dishes?.find { it.id == id }
                if (dish != null) {
                    Result.success(dish)
                } else {
                    Result.failure(Exception("Not Found value"))
                }
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    override suspend fun getDishes() {
        if (dishes.value.isSuccess && dishes.value.getOrNull()==null)
            refreshDishes()
    }

    override fun observeDishes(): StateFlow<Result<List<Dish>?>> {
        return dishes.asStateFlow()
    }

    override suspend fun deleteDishes(dishes: List<Dish>): Result<Unit> {
        val dishesEntity = dishes.map { it.toEntity() }
        return dishesLocalDataSources.deleteDishes(dishesEntity)
    }

}
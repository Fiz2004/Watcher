package com.faa.watcher.core.data.repository

import com.faa.watcher.core.data.sources.local.DishesLocalDataSource
import com.faa.watcher.core.data.sources.local.model.toEntity
import com.faa.watcher.core.data.sources.network.DishesNetworkDataSource
import com.faa.watcher.core.domain.model.Dish
import com.faa.watcher.core.domain.repository.DishesRepository
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

    private var dishes: MutableStateFlow<Result<List<Dish>?>> = MutableStateFlow(Result.success(null))

    init {
        externalScope.launch {

            launch {
                dishesLocalDataSources.observeDishes().collect {
                    dishes.value = Result.success(it?.map { it.toDomain() })
                }
            }

            launch {
                val dishesNetwork = dishesNetworkDataSources.getDishes()
                dishesNetwork.fold(

                    onSuccess = {
                        dishesLocalDataSources.saveDishes(it.map { it.toEntity() })
                    },

                    onFailure = {
                        dishes.value = Result.failure(it)
                    }

                )

            }
        }
    }

    override suspend fun getDish(id: String): Result<Dish> {
        return dishes.value.fold(
            onSuccess = {
                val dish = it?.find { it.id == id }
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

    override fun observeDishes(): StateFlow<Result<List<Dish>?>> {
        return dishes.asStateFlow()
    }

    override suspend fun deleteDishes(dishes: List<Dish>): Result<Unit> {
        val dishesEntity = dishes.map { it.toEntity() }
        return dishesLocalDataSources.deleteDishes(dishesEntity)
    }

}
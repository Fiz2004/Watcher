package com.faa.watcher.main.domain.usecase

import com.faa.watcher.main.domain.model.Dish
import com.faa.watcher.main.domain.repository.DishesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetObserveDishUseCase @Inject constructor(private val dishesRepository: DishesRepository) {
    operator fun invoke(): Flow<List<Dish>?> {
        return dishesRepository.dishes
    }
}
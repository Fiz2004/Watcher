package com.faa.watcher.core.domain.usecase

import com.faa.watcher.core.domain.model.Dish
import com.faa.watcher.core.domain.repository.DishesRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetObserveUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    operator fun invoke(): StateFlow<Result<List<Dish>?>> {
        return dishesRepository.observeDishes()
    }
}
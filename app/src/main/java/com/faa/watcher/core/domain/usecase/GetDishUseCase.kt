package com.faa.watcher.core.domain.usecase

import com.faa.watcher.core.domain.model.Dish
import com.faa.watcher.core.domain.repository.DishesRepository
import javax.inject.Inject

class GetDishUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    suspend operator fun invoke(id: String): Result<Dish> {
        return dishesRepository.getDish(id)
    }
}
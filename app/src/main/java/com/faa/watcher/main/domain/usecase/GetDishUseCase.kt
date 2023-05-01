package com.faa.watcher.main.domain.usecase

import com.faa.watcher.main.domain.model.Dish
import com.faa.watcher.main.domain.repository.DishesRepository
import javax.inject.Inject

class GetDishUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    suspend operator fun invoke(id: String): Dish {
        return dishesRepository.getDish(id)
    }
}
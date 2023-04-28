package com.faa.watcher.core.domain.usecase

import com.faa.watcher.core.domain.model.Dish
import com.faa.watcher.core.domain.repository.DishesRepository
import javax.inject.Inject

class DeleteDishesUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    suspend operator fun invoke(dishes: List<Dish>): Boolean {
        return dishesRepository.deleteDishes(dishes)
    }
}
package com.faa.watcher.main.domain.usecase

import com.faa.watcher.main.domain.repository.DishesRepository
import javax.inject.Inject

class DeleteDishesUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    suspend operator fun invoke(dishesIds: Set<String>) {
        return dishesRepository.deleteDishes(dishesIds)
    }
}
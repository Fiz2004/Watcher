package com.faa.watcher.core.domain.usecase

import com.faa.watcher.core.domain.repository.DishesRepository
import javax.inject.Inject

class GetDishesUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    suspend operator fun invoke() {
        return dishesRepository.getDishes()
    }
}
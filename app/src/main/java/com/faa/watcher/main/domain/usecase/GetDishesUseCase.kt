package com.faa.watcher.main.domain.usecase

import com.faa.watcher.main.domain.repository.DishesRepository
import javax.inject.Inject

class GetDishesUseCase @Inject constructor(private val dishesRepository: DishesRepository) {

    suspend operator fun invoke() {
        return dishesRepository.getDishes()
    }
}
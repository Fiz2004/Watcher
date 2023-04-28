package com.faa.watcher.core.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faa.watcher.core.domain.usecase.GetDishesUsecase
import com.faa.watcher.core.presentation.main.model.DishItemUi
import com.faa.watcher.core.presentation.main.model.MainEvent
import com.faa.watcher.core.presentation.main.model.MainViewEffect
import com.faa.watcher.core.presentation.main.model.MainViewState
import com.faa.watcher.core.presentation.main.model.toItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDishesUsecase: GetDishesUsecase
) : ViewModel() {

    private val _viewState = MutableStateFlow(MainViewState())
    val viewState = _viewState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<MainViewEffect>()
    val viewEffect = _viewEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            val dishes = getDishesUsecase()
            _viewState.value = _viewState.value
                .copy(dishes = dishes.map { it.toItemUi() })
        }
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.DeleteButtonClicked -> deleteButtonClicked()
            is MainEvent.DishClicked -> dishClicked(event.dish)
            is MainEvent.ChkSelectChanged -> chkSelectChanged(event.dish)
        }
    }

    private fun chkSelectChanged(dish: DishItemUi) {
        val newStateDishes = viewState.value.dishes.toMutableList()
        val index = newStateDishes.indexOfFirst { it.id == dish.id }
        if (index !in newStateDishes.indices)
            Log.d("MainViewModel.dishClicked", "index not range dishes")
        newStateDishes[index] = newStateDishes[index].copy(isChecked = !newStateDishes[index].isChecked)
        _viewState.value = viewState.value
            .copy(
                dishes = newStateDishes
            )
    }

    private fun deleteButtonClicked() {

    }

    private fun dishClicked(dish: DishItemUi) {

    }
}
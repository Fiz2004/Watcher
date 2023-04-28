package com.faa.watcher.core.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faa.watcher.core.domain.usecase.DeleteDishesUseCase
import com.faa.watcher.core.domain.usecase.GetDishesUseCase
import com.faa.watcher.core.domain.usecase.GetObserveUseCase
import com.faa.watcher.core.presentation.main.model.MainEvent
import com.faa.watcher.core.presentation.main.model.MainViewEffect
import com.faa.watcher.core.presentation.main.model.MainViewState
import com.faa.watcher.core.presentation.model.DishItemUi
import com.faa.watcher.core.presentation.model.toItemUi
import com.faa.watcher.utils.handleThrowable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getObserveUseCase: GetObserveUseCase,
    private val getDishesUseCase: GetDishesUseCase,
    private val deleteDishesUseCase: DeleteDishesUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(MainViewState(isLoading = true))
    val viewState = _viewState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<MainViewEffect>()
    val viewEffect = _viewEffect.asSharedFlow()

    init {
        getObserveUseCase().onEach { response ->
            response.fold(onSuccess = { dishes ->
                if (dishes != null) {
                    _viewState.value = _viewState.value.copy(
                        dishes = dishes.map { dish ->
                            dish.toItemUi()
                                .copy(isChecked = viewState.value.dishes?.find { dish.id == it.id }?.isChecked ?: false)
                        },
                        isLoading = false
                    )
                }
            }) { throwable ->
                _viewState.value = _viewState.value.copy(isLoading = false, isError = true)
                handleThrowable(throwable) { uiText ->
                    _viewEffect.emit(MainViewEffect.ShowMessage(uiText))
                }
            }
        }.flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.DeleteButtonClicked -> deleteButtonClicked()
            is MainEvent.DishClicked -> dishClicked(event.dish)
            is MainEvent.ChkSelectChanged -> chkSelectChanged(event.dish)
            MainEvent.ReloadData -> reloadData()
        }
    }

    private fun chkSelectChanged(dish: DishItemUi) {
        val newStateDishes = viewState.value.dishes?.toMutableList() ?: return
        val index = newStateDishes.indexOfFirst { it.id == dish.id }
        if (index !in newStateDishes.indices)
            Log.d("MainViewModel.dishClicked", "index not range dishes")
        newStateDishes[index] = newStateDishes[index].copy(isChecked = !newStateDishes[index].isChecked)
        _viewState.value = viewState.value
            .copy(dishes = newStateDishes)
    }

    private fun deleteButtonClicked() {
        _viewState.value = _viewState.value
            .copy(isLoading = true, isError = false)
        viewModelScope.launch {
            val selectedDishes = viewState.value.dishes?.filter { it.isChecked } ?: return@launch
            deleteDishesUseCase(selectedDishes.map { it.toDomain() })
        }
    }

    private fun dishClicked(dish: DishItemUi) {
        viewModelScope.launch {
            _viewEffect.emit(MainViewEffect.MoveDetailScreen(dish.id))
        }
    }

    private fun reloadData() {
        viewModelScope.launch {
            _viewState.value = viewState.value.copy(isLoading = true, isError = false)
            getDishesUseCase()
        }
    }
}
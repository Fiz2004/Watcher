package com.faa.watcher.main.presentation.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faa.watcher.common.handleThrowable
import com.faa.watcher.main.domain.model.Dish
import com.faa.watcher.main.domain.usecase.DeleteDishesUseCase
import com.faa.watcher.main.domain.usecase.GetDishesUseCase
import com.faa.watcher.main.domain.usecase.GetObserveDishUseCase
import com.faa.watcher.main.presentation.main.model.MainEvent
import com.faa.watcher.main.presentation.main.model.MainViewEffect
import com.faa.watcher.main.presentation.main.model.MainViewState
import com.faa.watcher.main.presentation.model.DishItemUi
import com.faa.watcher.main.presentation.model.toItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getObserveDishUseCase: GetObserveDishUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val getDishesUseCase: GetDishesUseCase,
    private val deleteDishesUseCase: DeleteDishesUseCase
) : ViewModel() {

    private val loadingState = MutableStateFlow(true)

    private val checkedSet = savedStateHandle.getStateFlow(CHECKED, setOf<String>())

    private val _viewEffect = MutableSharedFlow<MainViewEffect>()
    val viewEffect = _viewEffect.asSharedFlow()

    val viewState: StateFlow<MainViewState> =
        combine(getObserveDishUseCase(), checkedSet, loadingState, ::mergeFlow)
            .flowOn(Dispatchers.Default)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                MainViewState(isLoading = true)
            )

    private fun mergeFlow(
        dishFlow: Result<List<Dish>?>,
        checked: Set<String>,
        loading: Boolean
    ): MainViewState {
        return if (dishFlow.isSuccess)
                handleSuccessMerge(dishFlow.getOrNull(), checked, loading)
            else
                handleFailureMerge(dishFlow.exceptionOrNull())

    }

    private fun handleSuccessMerge(
        dishes: List<Dish>?,
        checked: Set<String>,
        loading: Boolean
    ): MainViewState {
        val newDishes = dishes?.map { dish ->
            dish.toItemUi()
                .copy(
                    isChecked = checked.find { dish.id == it } != null
                )
        }
        return MainViewState(dishes = newDishes, isLoading = loading)
    }

    private fun handleFailureMerge(throwable:Throwable?): MainViewState{
        handleThrowable(throwable) { uiText ->
            viewModelScope.launch {
                _viewEffect.emit(MainViewEffect.ShowMessage(uiText))
            }
        }
        return MainViewState(isLoading = false, isError = true)
    }

    init {
        reloadData()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.ChkSelectChanged -> chkSelectChanged(event.dish)
            is MainEvent.DishClicked -> dishClicked(event.dish)
            MainEvent.DeleteButtonClicked -> deleteButtonClicked()
            MainEvent.ReloadData -> reloadData()
        }
    }

    private fun chkSelectChanged(dish: DishItemUi) {
        val newSet = checkedSet.value.toMutableSet()
        if (!checkedSet.value.contains(dish.id)) {
            newSet.add(dish.id)
        } else {
            newSet.remove(dish.id)
        }
        savedStateHandle[CHECKED] = newSet
    }

    private fun dishClicked(dish: DishItemUi) {
        viewModelScope.launch {
            _viewEffect.emit(MainViewEffect.MoveDetailScreen(dish.id))
        }
    }

    private fun deleteButtonClicked() {
        loadingState.value = true
        viewModelScope.launch {
            val selectedDishes = viewState.value.dishes?.filter { it.isChecked } ?: return@launch
            deleteDishesUseCase(selectedDishes.map { it.toDomain() })
            loadingState.value = false
        }
    }

    private fun reloadData() {
        loadingState.value = true
        viewModelScope.launch {
            getDishesUseCase()
            loadingState.value = false
        }
    }

    companion object {
        private const val CHECKED = "checked"
    }
}
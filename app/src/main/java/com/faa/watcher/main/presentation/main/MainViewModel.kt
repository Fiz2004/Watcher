package com.faa.watcher.main.presentation.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faa.watcher.common.getUiText
import com.faa.watcher.di.DispatcherDefault
import com.faa.watcher.main.domain.usecase.DeleteDishesUseCase
import com.faa.watcher.main.domain.usecase.FetchDishesUseCase
import com.faa.watcher.main.domain.usecase.GetObserveDishUseCase
import com.faa.watcher.main.presentation.main.model.MainDishesItemUiState
import com.faa.watcher.main.presentation.main.model.MainDishesUiState
import com.faa.watcher.main.presentation.main.model.MainEvent
import com.faa.watcher.main.presentation.main.model.MainViewEffect
import com.faa.watcher.main.presentation.main.model.toMainDishItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    getObserveDishUseCase: GetObserveDishUseCase,
    @DispatcherDefault dispatcherDefault: CoroutineDispatcher,
    private val savedStateHandle: SavedStateHandle,
    private val fetchDishesUseCase: FetchDishesUseCase,
    private val deleteDishesUseCase: DeleteDishesUseCase
) : ViewModel() {

    private val dishesState = getObserveDishUseCase()
        .map { dishes -> dishes?.map { it.toMainDishItemUiState() } }
        .flowOn(dispatcherDefault)
    private val loadingState = MutableStateFlow(true)
    private val errorState = MutableStateFlow(false)
    private val selectState = savedStateHandle.getStateFlow(SELECT, setOf<String>())

    val uiState: StateFlow<MainDishesUiState> =
        combine(dishesState, selectState, loadingState, errorState)
        { dishes: List<MainDishesItemUiState>?, select: Set<String>, loading: Boolean, error: Boolean ->
            val newDishes = dishes?.map { dish ->
                dish.copy(isChecked = select.contains(dish.id))
            }
            MainDishesUiState(
                dishes = newDishes,
                isLoading = loading,
                isError = error
            )
        }
            .flowOn(dispatcherDefault)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                MainDishesUiState(isLoading = true)
            )

    private var fetchJob: Job? = null
    private var deleteJob: Job? = null

    private val _viewEffect = MutableSharedFlow<MainViewEffect>()
    val viewEffect = _viewEffect.asSharedFlow()

    private val deleteExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            handleThrowable(throwable)
            loadingState.value = false
        }
    }

    private val fetchExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            handleThrowable(throwable)
            loadingState.value = false
            errorState.value = true
        }
    }

    init {
        fetchDishes()
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.ChkSelectChanged -> chkSelectChanged(event.dish)
            is MainEvent.DishClicked -> dishClicked(event.dish)
            MainEvent.DeleteButtonClicked -> deleteButtonClicked()
            MainEvent.ReloadData -> fetchDishes()
        }
    }

    private fun chkSelectChanged(dish: MainDishesItemUiState) {
        val newSet = selectState.value.toMutableSet()
        if (!selectState.value.contains(dish.id)) {
            newSet.add(dish.id)
        } else {
            newSet.remove(dish.id)
        }
        savedStateHandle[SELECT] = newSet
    }

    private fun dishClicked(dish: MainDishesItemUiState) {
        viewModelScope.launch {
            _viewEffect.emit(MainViewEffect.MoveDetailScreen(dish.id))
        }
    }

    private fun deleteButtonClicked() {
        loadingState.value = true
        deleteJob?.cancel()
        deleteJob = viewModelScope.launch(deleteExceptionHandler) {
            deleteDishesUseCase(selectState.value)
            loadingState.value = false
        }
    }

    private fun fetchDishes() {
        loadingState.value = true
        errorState.value = false
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch(fetchExceptionHandler) {
            fetchDishesUseCase()
            loadingState.value = false
        }
    }

    private fun handleThrowable(throwable: Throwable) {
        viewModelScope.launch {
            getUiText(throwable) { uiText ->
                _viewEffect.emit(MainViewEffect.ShowMessage(uiText))
            }
        }
    }

    companion object {
        private const val SELECT = "SELECT"
    }
}
package com.faa.watcher.core.presentation.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.HttpException
import com.faa.watcher.R
import com.faa.watcher.core.domain.usecase.DeleteDishesUseCase
import com.faa.watcher.core.domain.usecase.GetDishesUseCase
import com.faa.watcher.core.presentation.main.model.DishItemUi
import com.faa.watcher.core.presentation.main.model.MainEvent
import com.faa.watcher.core.presentation.main.model.MainViewEffect
import com.faa.watcher.core.presentation.main.model.MainViewState
import com.faa.watcher.core.presentation.main.model.UiText
import com.faa.watcher.core.presentation.main.model.toItemUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDishesUseCase: GetDishesUseCase,
    private val deleteDishesUseCase: DeleteDishesUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(MainViewState())
    val viewState = _viewState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<MainViewEffect>()
    val viewEffect = _viewEffect.asSharedFlow()

    init {
        loadDishes()
    }

    private fun loadDishes() {
        _viewState.value = viewState.value
            .copy(isLoading = true, isError = false)
        getDishesUseCase().onEach {
            it.fold(
                onSuccess = {
                    if (it != null) {
                        _viewState.value = _viewState.value
                            .copy(dishes = it.map { it.toItemUi() }, isLoading = false)
                    }
                },
                onFailure = {
                    _viewState.value = _viewState.value
                        .copy(isLoading = false, isError = true)
                    when (it) {
                        is CancellationException -> {
                            throw it
                        }

                        is SocketTimeoutException -> {
                            _viewEffect.emit(
                                MainViewEffect.ShowMessage(
                                    UiText.StringResource(R.string.error_no_connection_server)
                                )
                            )
                        }

                        is HttpException -> {
                            _viewEffect.emit(
                                MainViewEffect.ShowMessage(
                                    UiText.DynamicString(it.message.toString())
                                )
                            )
                        }

                        is IOException -> {
                            _viewEffect.emit(
                                MainViewEffect.ShowMessage(
                                    UiText.StringResource(R.string.error_no_connection)
                                )
                            )
                        }

                        else -> {
                            Log.d("MainViewModel.loadDishes", it.message.toString())
                            _viewEffect.emit(MainViewEffect.ShowMessage(UiText.StringResource(R.string.error_unknown)))
                        }
                    }
                }
            )
        }.flowOn(Dispatchers.Default)
            .launchIn(viewModelScope)
    }

    fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.DeleteButtonClicked -> deleteButtonClicked()
            is MainEvent.DishClicked -> dishClicked(event.dish)
            is MainEvent.ChkSelectChanged -> chkSelectChanged(event.dish)
        }
    }

    private fun chkSelectChanged(dish: DishItemUi) {
        val newStateDishes = viewState.value.dishes?.toMutableList() ?: return
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
}
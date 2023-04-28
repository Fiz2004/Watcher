package com.faa.watcher.main.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faa.watcher.common.handleException
import com.faa.watcher.main.domain.usecase.GetDishUseCase
import com.faa.watcher.main.presentation.detail.model.DetailViewEffect
import com.faa.watcher.main.presentation.detail.model.DetailViewState
import com.faa.watcher.main.presentation.detail.model.toDetailDishItemUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDish: GetDishUseCase,
) : ViewModel() {

    val id: String? = savedStateHandle["id"]

    private val _uiState = MutableStateFlow(DetailViewState())
    val uiState = _uiState.asStateFlow()

    private val _viewEffect = MutableSharedFlow<DetailViewEffect>()
    val viewEffect = _viewEffect.asSharedFlow()

    init {
        viewModelScope.launch {
            if (id == null) {
                return@launch
            }
            try {
                val dish = getDish(id)
                _uiState.value = uiState.value
                    .copy(dish = dish.toDetailDishItemUiState())
            } catch (e: Exception) {
                handleException(e) { uiText ->
                    viewModelScope.launch {
                        _viewEffect.emit(DetailViewEffect.ShowMessage(uiText))
                    }
                }
            }

        }
    }
}
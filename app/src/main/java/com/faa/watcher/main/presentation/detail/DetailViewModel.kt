package com.faa.watcher.main.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faa.watcher.R
import com.faa.watcher.common.getUiText
import com.faa.watcher.common.model.UiText
import com.faa.watcher.main.domain.usecase.GetDishUseCase
import com.faa.watcher.main.presentation.detail.model.DetailViewEffect
import com.faa.watcher.main.presentation.detail.model.DetailViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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

    private val getExceptionHandler by lazy {
        CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch {
                getUiText(throwable) { uiText ->
                    _viewEffect.emit(DetailViewEffect.ShowMessage(uiText))
                }
            }
        }
    }

    init {
        viewModelScope.launch(getExceptionHandler) {
            if (id == null) {
                _viewEffect.emit(DetailViewEffect.ShowMessage(UiText.StringResource(R.string.error_id)))
                return@launch
            }
            val dish = getDish(id)
            _uiState.value = uiState.value
                .copy(dish = dish)
        }
    }
}
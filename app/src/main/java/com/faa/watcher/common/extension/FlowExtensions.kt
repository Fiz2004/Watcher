package com.faa.watcher.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun Fragment.launchAndRepeatWithViewLifecycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline block: suspend CoroutineScope.() -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(minActiveState) {
            block()
        }
    }
}

inline fun <ViewEffect> Fragment.collectUiEffect(
    viewEffect: SharedFlow<ViewEffect>,
    crossinline reactTo: (viewEffect: ViewEffect) -> Unit,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
) {
    launchAndRepeatWithViewLifecycle(minActiveState) {
        viewEffect.collect {
            reactTo.invoke(it)
        }
    }
}

inline fun <ViewState> Fragment.collectUiState(
    viewState: StateFlow<ViewState>,
    crossinline updateScreenState: (viewState: ViewState) -> Unit,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
) {
    launchAndRepeatWithViewLifecycle(minActiveState) {
        viewState.collectLatest {
            updateScreenState.invoke(it)
        }
    }
}
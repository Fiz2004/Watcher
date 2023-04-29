package com.faa.watcher.common

import android.util.Log
import com.bumptech.glide.load.HttpException
import com.faa.watcher.R
import com.faa.watcher.common.model.UiText
import kotlinx.coroutines.CancellationException
import java.io.IOException
import java.net.SocketTimeoutException

fun handleThrowable(t: Throwable?, action: (uiText: UiText) -> Unit) {
    when (t) {
        is CancellationException -> {
            throw t
        }

        else -> {
            val uiText = getUiText(t)
            action(uiText)
        }
    }
}

fun getUiText(t: Throwable?): UiText {
    return when (t) {
        is SocketTimeoutException -> {
            UiText.StringResource(R.string.error_no_connection_server)
        }

        is HttpException -> {
            UiText.DynamicString(t.message.toString())

        }

        is IOException -> {
            UiText.StringResource(R.string.error_no_connection)
        }

        else -> {
            Log.d("MainViewModel.loadDishes", t?.message.toString())
            UiText.StringResource(R.string.error_unknown)
        }
    }
}
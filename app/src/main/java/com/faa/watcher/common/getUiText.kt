package com.faa.watcher.common

import android.util.Log
import com.bumptech.glide.load.HttpException
import com.faa.watcher.R
import com.faa.watcher.common.model.UiText
import java.io.IOException
import java.net.SocketTimeoutException

suspend fun getUiText(throwable: Throwable, action: suspend (uiText: UiText) -> Unit) {
    val uiText = when (throwable) {
        is SocketTimeoutException -> {
            UiText.StringResource(R.string.error_no_connection_server)
        }

        is HttpException -> {
            UiText.DynamicString(throwable.message.toString())

        }

        is IOException -> {
            UiText.StringResource(R.string.error_no_connection)
        }

        else -> {
            Log.d("getUiText", throwable.message.toString())
            UiText.StringResource(R.string.error_unknown)
        }
    }
    action(uiText)
}

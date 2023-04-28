package com.faa.watcher.utils

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.faa.watcher.core.presentation.main.model.UiText


fun Fragment.showToast(uiText: UiText) {
    val text = when (uiText) {
        is UiText.DynamicString -> {
            uiText.value
        }

        is UiText.StringResource -> {
            getString(uiText.id)
        }
    }
    Toast.makeText(context, text, Toast.LENGTH_LONG).show()
}
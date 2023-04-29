package com.faa.watcher.common

import android.content.res.Resources
import android.util.TypedValue

fun Resources.dip(value: Int): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        value.toFloat(),
        displayMetrics
    ).toInt()
}
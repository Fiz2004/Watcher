package com.faa.watcher.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class LinearSpacingDecoration(
    private val marginStart: Int,
    private val marginTop: Int,
    private val marginEnd: Int,
    private val marginBottom: Int,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        outRect.apply {
            left = view.resources.dip(marginStart)
            top = view.resources.dip(marginTop)
            right = view.resources.dip(marginEnd)
            bottom = view.resources.dip(marginBottom)
        }
    }
}
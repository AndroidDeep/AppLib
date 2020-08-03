package com.self.lib.adapter.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import androidx.recyclerview.widget.RecyclerView.State
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * LinearLayout下RecyclerView分割线
 * @date 2019-11-27
 */
class LineDecoration(
    height: Int,
    color: Int = Color.parseColor("#00000000"),
    paddingLeft: Int = 0,
    paddingRight: Int = 0
) : ItemDecoration() {

    private var mColorDrawable: ColorDrawable = ColorDrawable(color)
    private var mHeight: Int = height
    private var mPaddingLeft = paddingLeft
    private var mPaddingRight = paddingRight

    private var mDrawAtLast = true

    private var startPosition = 0
    private var endPosition = -1

    fun setDrawAtLast(drawAtLast: Boolean) {
        mDrawAtLast = drawAtLast
    }

    fun setStartPosition(startPosition: Int) {
        if (startPosition < 0) return
        this.startPosition = startPosition
    }

    fun setEndPosition(endPosition: Int) {
        if (endPosition < 0) return
        this.endPosition = endPosition
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        if (parent.adapter == null) return
        val position = parent.getChildAdapterPosition(view)

        if (position < startPosition || (endPosition > -1 && endPosition < position)) {
            return
        }

        //最后一项不绘制
        if (position == parent.adapter?.itemCount ?: 0 - 1 && !mDrawAtLast) return

        var orientation = 0
        when (val layoutManager = parent.layoutManager) {
            is StaggeredGridLayoutManager -> {
                orientation = layoutManager.orientation
            }
            is GridLayoutManager -> {
                orientation = layoutManager.orientation
            }
            is LinearLayoutManager -> {
                orientation = layoutManager.orientation
            }
        }
        if (orientation == OrientationHelper.VERTICAL) {
            outRect.bottom = mHeight
        } else {
            outRect.right = mHeight
        }
    }

    override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: State
    ) {
        if (parent.adapter == null) {
            return
        }
        val layoutManager = parent.layoutManager
        val orientation = if (layoutManager is LinearLayoutManager) {
            layoutManager.orientation
        } else {
            return
        }
        val start: Int
        val end: Int
        if (orientation == OrientationHelper.VERTICAL) {
            start = parent.paddingLeft + mPaddingLeft
            end = parent.width - parent.paddingRight - mPaddingRight
        } else {
            start = parent.paddingTop + mPaddingLeft
            end = parent.height - parent.paddingBottom - mPaddingRight
        }
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)

            if (position < startPosition || (endPosition > -1 && endPosition < position)) {
                continue
            }
            //最后一项不绘制
            if (position == parent.adapter?.itemCount ?: 0 - 1 && !mDrawAtLast) continue

            if (orientation == OrientationHelper.VERTICAL) {
                val params = child.layoutParams as LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + mHeight
                mColorDrawable.setBounds(start, top, end, bottom)
                mColorDrawable.draw(c)
            } else {
                val params = child.layoutParams as LayoutParams
                val left = child.right + params.rightMargin
                val right = left + mHeight
                mColorDrawable.setBounds(left, start, right, end)
                mColorDrawable.draw(c)
            }
        }
    }
}
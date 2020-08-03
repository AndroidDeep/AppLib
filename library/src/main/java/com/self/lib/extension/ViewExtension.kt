package com.self.lib.extension

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewPropertyAnimator
import android.widget.TextView
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.self.lib.util.BarUtil

/**
 *
 * @author MBP
 * @date 2020/7/6
 */

inline val View.isLtr get() = layoutDirection == View.LAYOUT_DIRECTION_LTR

inline val View.isRtl get() = !isLtr

/**
 * Padding 相关
 * =================================================================================
 */
inline var View.topPadding: Int
    get() = paddingTop
    set(@Px value) = setPadding(paddingLeft, value, paddingRight, paddingBottom)

inline var View.bottomPadding: Int
    get() = paddingBottom
    set(@Px value) = setPadding(paddingLeft, paddingTop, paddingRight, value)

inline var View.leftPadding: Int
    get() = paddingLeft
    set(@Px value) = setPadding(value, paddingTop, paddingRight, paddingBottom)

inline var View.rightPadding: Int
    get() = paddingRight
    set(@Px value) = setPadding(paddingLeft, paddingTop, value, paddingBottom)

inline var View.startPadding: Int
    get() = paddingStart
    set(@Px value) = setPadding(
        if (isLtr) value else paddingLeft,
        paddingTop,
        if (isLtr) paddingRight else value,
        paddingBottom
    )

inline var View.endPadding: Int
    get() = paddingStart
    set(@Px value) = setPadding(
        if (isLtr) paddingLeft else value,
        paddingTop,
        if (isLtr) value else paddingRight,
        paddingBottom
    )

/*
 * =================================================================================
 */

inline fun View.onClick(crossinline block: () -> Unit) = setOnClickListener { block() }

inline fun View.onLongClick(
    consume: Boolean = true,
    crossinline block: () -> Unit
) = setOnLongClickListener { block(); consume }

/**
 * View添加水波纹
 */
fun View.addRippleForeground() {
    if (VERSION.SDK_INT >= VERSION_CODES.M) {
        val outValue = TypedValue()
        context.theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            outValue,
            true
        )
        foreground = ContextCompat.getDrawable(context, outValue.resourceId)
    }
}

/**
 * View的顶部添加状态栏空间，一般用于沉浸状态栏之后
 */
fun View.addStatusBarPadding() {
    topPadding = paddingTop + BarUtil.getStatusBarHeight()
    invalidate()
}

/**
 * View添加点击缩放动画
 */
@SuppressLint("ClickableViewAccessibility")
fun View.addTouchShake() {
    setOnTouchListener { _, event ->
        var downAnimator: ViewPropertyAnimator? = null
        if (event.action == MotionEvent.ACTION_DOWN) {
            downAnimator = this.animate().scaleX(0.8f).scaleY(0.8f).setDuration(100).apply { start() }
        }

        if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
            downAnimator?.cancel()
            this.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
        }
        false
    }
}

/**
 * 将一个控件的enable与其它几个文本控件关联
 */
fun View.bindEnableWith(vararg view: TextView) {
    view.forEach { v ->
        v.doOnTextChanged { _, _, _, _ ->
            this.isEnabled = view.none { it.text().isBlank() }
        }
    }
}

/**
 * 生成view的bitmap图片
 * @receiver View
 */
fun View.createBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, ARGB_8888)
    draw(Canvas(bitmap))
    return bitmap
}
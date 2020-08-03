package com.self.lib.extension

import android.widget.TextView
import androidx.annotation.DrawableRes

/**
 * TextView拓展
 * @date 2020/7/6
 */

fun TextView.text() = text.toString()

fun TextView.trimText() = text.toString().trimStart().trimEnd()

fun TextView.setDrawableTop(@DrawableRes imgRes:Int){
    setCompoundDrawablesWithIntrinsicBounds(0,imgRes,0,0)
}

fun TextView.setDrawableBottom(@DrawableRes imgRes:Int){
    setCompoundDrawablesWithIntrinsicBounds(0,0,0,imgRes)
}

fun TextView.setDrawableStart(@DrawableRes imgRes:Int){
    setCompoundDrawablesWithIntrinsicBounds(if(isLtr) imgRes else 0,0, if(isLtr) 0 else imgRes,0)
}

fun TextView.setDrawableEnd(@DrawableRes imgRes:Int){
    setCompoundDrawablesWithIntrinsicBounds(if(isLtr) 0 else imgRes,0, if(isLtr) imgRes else 0,0)
}

fun TextView.clearCompoundDrawables() = setCompoundDrawables(null, null, null, null)
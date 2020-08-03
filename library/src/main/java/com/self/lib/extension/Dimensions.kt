@file:Suppress("NOTHING_TO_INLINE")

package com.self.lib.extension

import android.content.res.Resources

@PublishedApi
internal var density = Resources.getSystem().displayMetrics.density

@PublishedApi
internal var scaledDensity = Resources.getSystem().displayMetrics.scaledDensity

inline fun dp(dpValue: Float) : Float = dpValue * density

inline fun dp(dpValue: Int) : Int = (dpValue * density + 0.5f).toInt()

inline fun px(pxValue: Float) : Float = pxValue / density

inline fun px(pxValue: Int) : Int = (pxValue / density + 0.5f).toInt()

inline fun sp(spValue: Float) : Float = spValue * scaledDensity

inline fun sp(spValue: Int) : Int = (spValue * scaledDensity + 0.5f).toInt()

inline fun px2sp(pxValue: Float) : Float = pxValue / scaledDensity

inline fun px2sp(pxValue: Int) : Int = (pxValue / scaledDensity + 0.5f).toInt()
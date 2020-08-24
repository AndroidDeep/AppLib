package com.self.lib.util

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import androidx.annotation.NonNull
import splitties.init.appCtx
import splitties.systemservices.windowManager

object ScreenUtil {

    private val wm = appCtx.windowManager

    private var screenWidth = 0
    private var screenHeight = 0

    /**
     * 获取屏幕宽度
     */
    @JvmStatic
    fun getScreenWidth(): Int {
        if(screenWidth == 0){
            val point = Point()
            wm.defaultDisplay.getRealSize(point)
            screenWidth = point.x
        }
        return screenWidth
    }

    @JvmStatic
    fun getScreenWidthDp():Int = appCtx.resources.configuration.screenWidthDp

    @JvmStatic
    fun getScreenHeightDp():Int = appCtx.resources.configuration.screenHeightDp

    /**
     * 获取屏幕高度
     */
    @JvmStatic
    fun getScreenHeight(): Int {
        if(screenHeight == 0){
            val point = Point()
            wm.defaultDisplay.getRealSize(point)
            screenHeight =  point.y
        }
        return screenHeight
    }

    /**
     * 获取app所占的宽度(分屏模式)
     */
    @JvmStatic
    fun getAppScreenWidth(): Int {
        val point = Point()
        wm.defaultDisplay.getSize(point)
        return point.x
    }

    /**
     * 获取app所占的高度(分屏模式)
     */
    @JvmStatic
    fun getAppScreenHeight(): Int {
        val point = Point()
        wm.defaultDisplay.getSize(point)
        return point.y
    }

    /**
     * 设置页面为横屏
     */
    @JvmStatic
    fun setLandscape(@NonNull activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    /**
     * 设置页面为竖屏
     */
    @JvmStatic
    fun setPortrait(@NonNull activity: Activity) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 判断当前是否为横屏状态
     */
    @JvmStatic
    fun isLandscape(): Boolean {
        return (appCtx.resources.configuration.orientation
                == Configuration.ORIENTATION_LANDSCAPE)
    }

    /**
     * 判断当前是否为竖屏状态
     */
    @JvmStatic
    fun isPortrait(): Boolean {
        return (appCtx.resources.configuration.orientation
                == Configuration.ORIENTATION_PORTRAIT)
    }


}
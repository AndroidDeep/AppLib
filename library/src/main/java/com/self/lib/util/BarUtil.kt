package com.self.lib.util

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import splitties.init.appCtx

/**
 *
 * 状态栏与导航栏相关方法工具类
 */

object BarUtil {

    ///////////////////////////////////////////////////////////////////////////
    // 状态栏

    private var statusBarHeight: Int = 0

    /**
     * 获取状态栏高度
     */
    @JvmStatic
    fun getStatusBarHeight(): Int {
        if (statusBarHeight == 0) {
            val resources = appCtx.resources
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            statusBarHeight = resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    /**
     * 状态栏沉浸效果
     */
    @JvmStatic
    fun transparentStatusBar(window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val vis = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = option or vis
        } else {
            window.decorView.systemUiVisibility = option
        }
        window.statusBarColor = Color.TRANSPARENT
    }

    /**
     * 设置状态栏亮色效果，亮色效果下状态栏字体为黑色，API>=M
     */
    @JvmStatic
    fun setStatusBarLightMode(
        window: Window, isLightMode: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            var vis = decorView.systemUiVisibility
            vis = if (isLightMode) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decorView.systemUiVisibility = vis
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 导航栏

    private var navigationBarHeight: Int = 0

    /**
     * 获取导航栏高度
     */
    fun getNavBarHeight(): Int {
        if (navigationBarHeight == 0) {
            val res = appCtx.resources
            val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId != 0) {
                navigationBarHeight = res.getDimensionPixelSize(resourceId)
            }
        }
        return navigationBarHeight
    }

    /**
     * 设置导航栏可见性
     */
    fun setNavBarVisibility(window: Window, isVisible: Boolean) {
        val decorView = window.decorView as ViewGroup
        var i = 0
        val count = decorView.childCount
        while (i < count) {
            val child = decorView.getChildAt(i)
            val id = child.id
            if (id != View.NO_ID) {
                val resourceEntryName = appCtx
                    .resources
                    .getResourceEntryName(id)
                if ("navigationBarBackground" == resourceEntryName) {
                    child.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
                }
            }
            i++
        }
        val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (isVisible) {
            decorView.systemUiVisibility = decorView.systemUiVisibility and uiOptions.inv()
        } else {
            decorView.systemUiVisibility = decorView.systemUiVisibility or uiOptions
        }
    }

    /**
     * 判断导航栏是否可见
     */
    fun isNavBarVisible(window: Window): Boolean {
        var isVisible = false
        val decorView = window.decorView as ViewGroup
        var i = 0
        val count = decorView.childCount
        while (i < count) {
            val child = decorView.getChildAt(i)
            val id = child.id
            if (id != View.NO_ID) {
                val resourceEntryName = appCtx
                    .resources
                    .getResourceEntryName(id)
                if ("navigationBarBackground" == resourceEntryName && child.visibility == View.VISIBLE) {
                    isVisible = true
                    break
                }
            }
            i++
        }
        if (isVisible) {
            val visibility = decorView.systemUiVisibility
            isVisible = visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
        }
        return isVisible
    }

    /**
     * 设置导航栏颜色
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setNavBarColor(window: Window, @ColorInt color: Int) {
        window.navigationBarColor = color
    }

    /**
     * 判断是否有导航栏虚拟键
     */
    fun isSupportNavBar(): Boolean {
        val wm = appCtx.getSystemService(Context.WINDOW_SERVICE) as WindowManager? ?: return false
        val display = wm.defaultDisplay
        val size = Point()
        val realSize = Point()
        display.getSize(size)
        display.getRealSize(realSize)
        return realSize.y != size.y || realSize.x != size.x
    }
}
package com.self.lib.extension

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import com.self.lib.util.BarUtil
import splitties.systemservices.inputMethodManager

/**
 *
 * Activity 拓展方法
 * @date 2020/7/4
 */

/**
 * 设置状态栏沉浸
 */
fun Activity.setTransparentStatusBar(){
    BarUtil.transparentStatusBar(window)
}

/**
 * 设置状态栏是否亮色效果，亮色时状态栏字体为黑色
 */
fun Activity.setStatusBarLightMode(lightMode:Boolean){
    BarUtil.setStatusBarLightMode(window,lightMode)
}

/**
 * 结束退出当前activity
 */
fun Activity.goBack(){
    ActivityCompat.finishAfterTransition(this)
}

/**
 * 隐藏当前界面的软键盘
 */
fun Activity.hideSoftboard() {
    val view = currentFocus?: View(this)
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * 从颜色资源中获取颜色
 */
fun Activity.getResColor(@ColorRes color: Int) =
    ContextCompat.getColor(this,color)

/**
 * 共享元素跳转
 */
fun Activity.startWithElement(intent: Intent,vararg sharedElements:View?){
    val pairs = sharedElements.filterNotNull().map {
        Pair.create(it, it.transitionName)
    }.toTypedArray()

    this.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs).toBundle())
}

fun Activity.startForResultWithElement(intent:Intent, requestCode:Int, vararg sharedElements:View?){

    val pairs = sharedElements.filterNotNull().map {
        Pair.create(it, it.transitionName)
    }.toTypedArray()

    this.startActivityForResult(intent, requestCode, ActivityOptionsCompat.makeSceneTransitionAnimation(this, *pairs).toBundle())
}
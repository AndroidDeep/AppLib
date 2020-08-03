package com.self.lib.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import splitties.init.appCtx

/**
 *
 * Fragment 拓展方法
 * @date 2020/7/4
 */

/**
 * Fragment获取context
 */
val Fragment.ctx: Context
    get() = context ?: appCtx

/**
 * 设置状态栏沉浸
 */
fun Fragment.setTransparentStatusBar(){
    activity?.setTransparentStatusBar()
}

/**
 * 设置状态栏是否亮色效果，亮色时状态栏字体为黑色
 */
fun Fragment.setStatusBarLightMode(lightMode:Boolean){
    activity?.setStatusBarLightMode(lightMode)
}

/**
 * 结束当前Fragment所属的Activity
 */
fun Fragment.finishActivity(){
    activity?.goBack()
}

/**
 * 共享元素跳转
 */
fun Fragment.startWithElement(intent: Intent,vararg sharedElements: View?){
    activity?.startWithElement(intent,*sharedElements)
}

fun Fragment.startForResultWithElement(intent:Intent, requestCode:Int, vararg sharedElements:View?){
    activity?.startForResultWithElement(intent, requestCode, *sharedElements)
}

inline fun <reified A : Activity> Fragment.start(configIntent: Intent.() -> Unit = {}) {
    startActivity(Intent(ctx, A::class.java).apply(configIntent))
}

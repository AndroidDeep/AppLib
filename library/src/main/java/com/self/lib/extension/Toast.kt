@file:Suppress("NOTHING_TO_INLINE")

package com.self.lib.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.os.Build.VERSION
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.transition.Slide.GravityFlag
import splitties.init.appCtx
import splitties.systemservices.layoutInflater
import splitties.systemservices.windowManager
import kotlin.LazyThreadSafetyMode.NONE

/**
 * toast工具
 * @date 2020/7/6
 */

@PublishedApi
internal var mToast: Toast? = null

@PublishedApi
internal var mGravity: Int? = null

@PublishedApi
internal fun Context.createToast(text: CharSequence, duration: Int): Toast {
    val ctx = if (VERSION.SDK_INT == 25) SafeToastCtx(this) else this
    return Toast.makeText(ctx, text, duration)
}

@PublishedApi
internal fun Context.createToast(@StringRes resId: Int, duration: Int): Toast {
    return createToast(getText(resId), duration)
}

inline fun Context.toast(msg: CharSequence?, @GravityFlag gravity: Int? = mGravity) {
    if(msg.isNullOrBlank())  return
    mToast?.cancel()
    mToast = createToast(msg, Toast.LENGTH_SHORT).apply {
        if (gravity != null)
            setGravity(gravity, 0, 0)
        show()
    }
}

inline fun Fragment.toast(msg: CharSequence?, @GravityFlag gravity: Int? = mGravity) = ctx.toast(msg, gravity)
inline fun View.toast(msg: CharSequence?, @GravityFlag gravity: Int? = mGravity) = context.toast(msg, gravity)
inline fun toast(msg: CharSequence?, @GravityFlag gravity: Int? = mGravity) = appCtx.toast(msg, gravity)

inline fun Context.toast(@StringRes msgResId: Int, @GravityFlag gravity: Int? = mGravity) =
    toast(getText(msgResId), gravity)

inline fun Fragment.toast(@StringRes msgResId: Int, @GravityFlag gravity: Int? = mGravity) =
    ctx.toast(msgResId, gravity)

inline fun View.toast(@StringRes msgResId: Int, @GravityFlag gravity: Int? = mGravity) =
    context.toast(msgResId, gravity)

inline fun toast(@StringRes msgResId: Int, @GravityFlag gravity: Int? = mGravity) = appCtx.toast(msgResId, gravity)

inline fun Context.longToast(msg: CharSequence?, @GravityFlag gravity: Int? = mGravity) {
    if(msg.isNullOrBlank())  return
    mToast?.cancel()
    mToast = createToast(msg, Toast.LENGTH_LONG).apply {
        if (gravity != null)
            setGravity(gravity, 0, 0)
        show()
    }
}

inline fun Fragment.longToast(msg: CharSequence?, @GravityFlag gravity: Int? = mGravity) = ctx.longToast(msg, gravity)
inline fun View.longToast(msg: CharSequence?, @GravityFlag gravity: Int? = mGravity) = context.longToast(msg, gravity)
inline fun longToast(msg: CharSequence?, @GravityFlag gravity: Int? = mGravity) = appCtx.longToast(msg, gravity)

inline fun Context.longToast(@StringRes msgResId: Int, @GravityFlag gravity: Int? = mGravity) =
    longToast(getText(msgResId), gravity)

inline fun Fragment.longToast(@StringRes msgResId: Int, @GravityFlag gravity: Int? = mGravity) =
    ctx.longToast(msgResId, gravity)

inline fun View.longToast(@StringRes msgResId: Int, @GravityFlag gravity: Int? = mGravity) =
    context.longToast(msgResId,gravity)

inline fun longToast(@StringRes msgResId: Int, @GravityFlag gravity: Int? = mGravity) = appCtx.longToast(msgResId,gravity)

inline fun setToastGravity(@GravityFlag gravity: Int){
    mGravity = gravity
}

inline fun cancelAllToast() = mToast?.cancel()

/**
 * Avoids [WindowManager.BadTokenException] on API 25.
 */
private class SafeToastCtx(ctx: Context) : ContextWrapper(ctx) {

    private val toastWindowManager by lazy(NONE) {
        ToastWindowManager(
            baseContext.windowManager
        )
    }
    private val toastLayoutInflater by lazy(NONE) {
        baseContext.layoutInflater.cloneInContext(this)
    }

    override fun getApplicationContext(): Context =
        SafeToastCtx(baseContext.applicationContext)
    override fun getSystemService(name: String): Any = when (name) {
        Context.LAYOUT_INFLATER_SERVICE -> toastLayoutInflater
        Context.WINDOW_SERVICE -> toastWindowManager
        else -> super.getSystemService(name)
    }

    private class ToastWindowManager(private val base: WindowManager) : WindowManager by base {

        @SuppressLint("LogNotTimber") // Timber is not a dependency here, but lint passes through.
        override fun addView(view: View?, params: ViewGroup.LayoutParams?) {
            try {
                base.addView(view, params)
            } catch (e: WindowManager.BadTokenException) {
                Log.e("SafeToast", "Couldn't add Toast to WindowManager", e)
            }
        }
    }
}
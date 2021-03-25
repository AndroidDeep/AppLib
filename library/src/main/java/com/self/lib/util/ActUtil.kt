package com.self.lib.util

import android.app.Activity
import java.lang.ref.WeakReference

/**
 *
 * @date 2020/8/23
 */
class ActUtil private constructor() {

    companion object {
        @Volatile
        private var instance: ActUtil? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: ActUtil().also { instance = it }
            }
    }

    private var currentAct: WeakReference<Activity>? = null

    fun setCurrentActivity(act: Activity) {
        currentAct = WeakReference(act)
    }

    fun getCurrentActivity() = currentAct?.get()
}
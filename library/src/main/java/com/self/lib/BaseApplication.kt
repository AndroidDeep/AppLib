package com.self.lib

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.jeremyliao.liveeventbus.LiveEventBus
import com.self.lib.util.ActUtil
import splitties.init.injectAsAppCtx
import java.util.concurrent.Executors

/**
 *
 * @author MBP
 * @date 3/17/21
 */
abstract class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        injectAsAppCtx()

        Executors.newSingleThreadExecutor().execute {
            LiveEventBus.config().lifecycleObserverAlwaysActive(false)
            initialize()
        }

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(act: Activity) {}

            override fun onActivityStarted(act: Activity) {
                ActUtil.getInstance().setCurrentActivity(act)
            }

            override fun onActivityDestroyed(act: Activity) {}

            override fun onActivitySaveInstanceState(act: Activity, savedState: Bundle) {}

            override fun onActivityStopped(act: Activity) {}

            override fun onActivityCreated(act: Activity, savedState: Bundle?) {}

            override fun onActivityResumed(act: Activity) {
                ActUtil.getInstance().setCurrentActivity(act)
            }
        })
    }

    abstract fun initialize()

}
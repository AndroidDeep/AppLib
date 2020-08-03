package com.self.lib.util

import android.app.ActivityManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Process
import android.provider.Settings
import splitties.init.appCtx
import splitties.systemservices.activityManager
import kotlin.system.exitProcess

object AppUtil{

    /**
     * 获取版本名
     */
    @JvmStatic
    fun getAppVersionName(): String? {
        return try {
            val pi = appCtx.packageManager.getPackageInfo(appCtx.packageName, 0)
            pi?.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 获取版本号
     */
    @JvmStatic
    fun getAppVersionCode() : Int{
        return try {
            val pi = appCtx.packageManager.getPackageInfo(appCtx.packageName, 0)
            @Suppress("DEPRECATION")
            pi?.versionCode ?: 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0
        }
    }

    @JvmStatic
    fun exitApp(){
//        ActivityUtil.finishAllActivities()
//        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    @JvmStatic
    fun relaunchApp() {
        appCtx.run {
            val intent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)
        }
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    /**
     * 判断应用是否在前台
     */
    @JvmStatic
    fun isAppForeground(): Boolean {
        val am = activityManager
        val info = am.runningAppProcesses
        if (info == null || info.size == 0) return false
        for (aInfo in info) {
            if (aInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName == appCtx.packageName
            }
        }
        return false
    }

    /**
     * 打开应用的系统设置
     */
    @JvmStatic
    fun launchAppDetailsSettings(packageName: String? = null) {
        val packName = packageName?: appCtx.packageName
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packName")
        appCtx.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

}
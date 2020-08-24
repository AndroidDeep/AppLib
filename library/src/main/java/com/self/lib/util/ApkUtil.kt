package com.self.lib.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.content.FileProvider
import splitties.init.appCtx
import java.io.File

object ApkUtil {

    /**
     * 安装一个apk
     *
     * @param context     上下文
     * @param authorities Android N 授权
     * @param apk         安装包文件
     */
    @JvmStatic
    fun installApk(
        context: Context,
        authorities: String,
        apk: File
    ) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addCategory(Intent.CATEGORY_DEFAULT)
            val uri: Uri
            if (VERSION.SDK_INT >= VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(context, authorities, apk)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                uri = Uri.fromFile(apk)
            }
            setDataAndType(uri, "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }

    /**
     * 对一个apk文件获取相应的信息
     * @param path    apk路径
     */
    fun getVersionCodeByPath(path: String): Int {
        val packageInfo =
            appCtx.packageManager.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
                ?: return 0
        @Suppress("DEPRECATION")
        return packageInfo.versionCode
    }

    /**
     * 对一个apk文件获取相应的信息
     * @param apkFile    apk文件
     */
    fun getVersionCodeByPath(apkFile: File): Int {
        val packageInfo =
            appCtx.packageManager.getPackageArchiveInfo(apkFile.path, PackageManager.GET_ACTIVITIES)
                ?: return 0
        @Suppress("DEPRECATION")
        return packageInfo.versionCode
    }
}
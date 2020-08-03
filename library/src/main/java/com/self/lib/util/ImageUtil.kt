package com.self.lib.util

import android.Manifest.permission
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap.CompressFormat.JPEG
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.MediaStore.Images.Media
import android.provider.MediaStore.MediaColumns.DATA
import android.provider.MediaStore.MediaColumns.DATE_ADDED
import android.provider.MediaStore.MediaColumns.DATE_MODIFIED
import android.provider.MediaStore.MediaColumns.DISPLAY_NAME
import android.provider.MediaStore.MediaColumns.IS_PENDING
import android.provider.MediaStore.MediaColumns.MIME_TYPE
import android.provider.MediaStore.MediaColumns.RELATIVE_PATH
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.contentValuesOf
import com.self.lib.extension.logError
import splitties.init.appCtx
import java.io.File
import java.io.IOException
import java.io.OutputStream

/**
 * 图像工具类
 * @author MBP
 * @date 2020/7/11
 */
object ImageUtil {

    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @param context
     * @return 图片的uri
     */
    @JvmStatic
    fun createImageUri(context: Context): Uri? {
        val imageFilePath = arrayOfNulls<Uri>(1)
        val time = System.currentTimeMillis()
        val values = contentValuesOf(
            Media.DISPLAY_NAME to time.toString(),
            Media.MIME_TYPE to "image/jpeg"
        )

        if (VERSION.SDK_INT >= VERSION_CODES.Q) {
            values.put(Media.DATE_TAKEN, time)
        }

        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            imageFilePath[0] =
                context.contentResolver.insert(Media.EXTERNAL_CONTENT_URI, values)
        } else {
            imageFilePath[0] =
                context.contentResolver.insert(Media.INTERNAL_CONTENT_URI, values)
        }
        return imageFilePath[0]
    }

    fun save2Album(
        src: Bitmap,
        fileName: String,
        format: CompressFormat,
        quality: Int = 100
    ): Boolean {
        val suffix = if (JPEG == format) "JPG" else format.name
        val savedName = "$fileName.$suffix"

        val now = System.currentTimeMillis() / 1000
        val contentValues = ContentValues().apply {
            put(DISPLAY_NAME, savedName)
            put(MIME_TYPE, "image/*")
            put(DATE_ADDED, now)
            put(DATE_MODIFIED, now)
        }

        var savedFile : File? = null
        if (VERSION.SDK_INT < VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(appCtx, permission.WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
                logError("save to album need storage permission")
                return false
            }
            @Suppress("DEPRECATION")
            val picDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            savedFile = File(picDir, "Camera${File.separator}${savedName}")
            @Suppress("DEPRECATION")
            contentValues.put(DATA, savedFile.absolutePath)
        } else {
            contentValues.put(IS_PENDING, 1)
            contentValues.put(
                RELATIVE_PATH, "${Environment.DIRECTORY_DCIM}${File.separator}Camera"
            )
        }
        val contentUri: Uri = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Media.EXTERNAL_CONTENT_URI
        } else {
            Media.INTERNAL_CONTENT_URI
        }

        val resolver = appCtx.contentResolver
        val uri: Uri = resolver.insert(contentUri, contentValues) ?: return false
        var os: OutputStream? = null
        try {
            os = appCtx.contentResolver.openOutputStream(uri)
            os?.let {
                src.compress(format, quality, it)
            }
        } catch (e: Exception) {
            resolver.delete(uri, null, null)
            e.printStackTrace()
            return false
        } finally {
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        if (VERSION.SDK_INT < VERSION_CODES.Q){
            savedFile?.let { file ->
                @Suppress("DEPRECATION")
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                    data = Uri.parse("file://" + file.absolutePath)
                }
                appCtx.sendBroadcast(intent)
                return true
            }
            return false
        }else{
            contentValues.put(IS_PENDING, 0)
            return true
        }
    }

    private fun isEmptyBitmap(src: Bitmap?): Boolean {
        return src == null || src.width == 0 || src.height == 0
    }
}
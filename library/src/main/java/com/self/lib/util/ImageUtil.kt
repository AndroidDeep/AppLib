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
}
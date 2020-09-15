package com.self.lib.util

import android.content.Context
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.MediaStore.Images.Media
import androidx.core.content.contentValuesOf

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
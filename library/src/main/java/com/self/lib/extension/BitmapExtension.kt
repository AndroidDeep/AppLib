package com.self.lib.extension

import android.Manifest.permission
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Environment
import android.provider.MediaStore.Images.ImageColumns
import android.provider.MediaStore.Images.Media
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import splitties.init.appCtx
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream

/**
 * @date 2020/9/1
 */

/**
 * 保存图片至相册
 * @receiver Bitmap
 * @param fileName String
 * @param format CompressFormat
 * @param deleteSameName Boolean     //是否删除同目录同名旧文件，Android Q及以上才可用，低版本强制删除
 * @param quality Int
 * @param recycle Boolean
 * @return Boolean
 */
@Suppress("DEPRECATION")
fun Bitmap.saveToAlbum(
    fileName: String = System.currentTimeMillis().toString(),
    format: CompressFormat = JPEG,
    deleteSameName: Boolean = true,
    quality: Int = 100,
    recycle: Boolean = true
): Boolean {
    val suffix = if (JPEG == format) "jpg" else format.name
    val savedName = "$fileName.$suffix"

    val now = System.currentTimeMillis() / 1000
    val contentValues = ContentValues().apply {
        put(ImageColumns.DISPLAY_NAME, savedName)
        put(ImageColumns.MIME_TYPE, "image/*")
        put(ImageColumns.DATE_ADDED, now)
        put(ImageColumns.DATE_MODIFIED, now)
    }

    @Suppress("DEPRECATION")
    val dcmDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
    val cameraDir = File(dcmDir, "Camera")
    val albumDir = if (cameraDir.exists() && cameraDir.isDirectory) cameraDir else dcmDir

    val savedFile = File(albumDir, savedName)

    if (VERSION.SDK_INT < VERSION_CODES.Q) {
        if (ContextCompat.checkSelfPermission(
                appCtx,
                permission.WRITE_EXTERNAL_STORAGE
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            logError("save to album need storage permission")
            return false
        }

        contentValues.put(ImageColumns.DATA, savedFile.absolutePath)
    } else {
        contentValues.put(ImageColumns.IS_PENDING, 1)
        contentValues.put(
            ImageColumns.RELATIVE_PATH,
            "${Environment.DIRECTORY_DCIM}${albumDir.path.substringAfter(Environment.DIRECTORY_DCIM)}"
        )
    }

    val contentUri = if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        Media.EXTERNAL_CONTENT_URI
    } else {
        Media.INTERNAL_CONTENT_URI
    }
    val resolver = appCtx.contentResolver

    //insert will create new filename end with (1),(2) above android Q,but below can not,so must delete old file
    if(deleteSameName || VERSION.SDK_INT < VERSION_CODES.Q) {
        getExistingImageUriOrNull(contentUri, contentValues)?.let {
            resolver.delete(it, null, null)
        }
    }

    val uri = resolver.insert(contentUri, contentValues) ?: return false
    var os: OutputStream? = null
    try {
        os = resolver.openOutputStream(uri)
        os?.let {
            this.compress(format, quality, it)
        }
        contentValues.clear()
        if (recycle && !this.isRecycled) this.recycle()
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

    if (VERSION.SDK_INT < VERSION_CODES.Q) {
        savedFile.let { file ->
            @Suppress("DEPRECATION")
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
                data = Uri.parse("file://" + file.absolutePath)
            }
            appCtx.sendBroadcast(intent)
            return true
        }
    } else {
        contentValues.put(ImageColumns.IS_PENDING, 0)
        resolver.update(uri, contentValues, null, null)
        return true
    }
}

@Suppress("DEPRECATION")
private fun getExistingImageUriOrNull(contentUri:Uri,contentValues:ContentValues): Uri? {
    val projection = arrayOf(
        ImageColumns._ID,
        ImageColumns.DISPLAY_NAME,
        ImageColumns.DATA
    )

    val selection = if (VERSION.SDK_INT < VERSION_CODES.Q) {
        "${ImageColumns.DATA}=? AND ${ImageColumns.DISPLAY_NAME}=?"
    }else{
        "${ImageColumns.RELATIVE_PATH}=? AND ${ImageColumns.DISPLAY_NAME}=?"
    }

    val selectionArgs = if (VERSION.SDK_INT < VERSION_CODES.Q) {
        arrayOf(contentValues.getAsString(ImageColumns.DATA)
            ,contentValues.getAsString(ImageColumns.DISPLAY_NAME))
    }else{
        arrayOf("${contentValues.getAsString(ImageColumns.RELATIVE_PATH)}/"
            ,contentValues.getAsString(ImageColumns.DISPLAY_NAME))
    }

    appCtx.contentResolver.query(contentUri,
        projection, selection, selectionArgs, null ).use { c ->
        if (c != null && c.count >= 1) {
            c.moveToFirst().let {
                val id = c.getLong(c.getColumnIndexOrThrow(ImageColumns._ID) )
                return ContentUris.withAppendedId(contentUri,  id)
            }
        }
    }
    return null
}

fun Bitmap?.isEmpty() = this == null || width == 0 || height == 0

fun Bitmap.toByteArray(needRecycle: Boolean = true):ByteArray{
    val output = ByteArrayOutputStream()
    compress(PNG, 100, output)
    if (needRecycle) {
        recycle()
    }
    val result = output.toByteArray()
    try {
        output.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}

fun Bitmap.compress(format: CompressFormat,quality: Int):Bitmap{
    val output = ByteArrayOutputStream()
    compress(format, quality, output)
    recycle()
    val byteArray = output.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
}
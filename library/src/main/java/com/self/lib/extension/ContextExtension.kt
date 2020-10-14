package com.self.lib.extension

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import androidx.core.content.ContextCompat

/**
 * 从资源中获取颜色
 */
fun Context.getColorRes(@ColorRes color:Int) = ContextCompat.getColor(this,color)

/**
 * 根据透明值获取带有透明效果的颜色
 * @param alpha 透明程度 0(全透明)-1(不透明)
 */
fun Context.getAlphaColor(@ColorRes color:Int, @FloatRange(from = 0.0, to = 1.0) alpha:Float) : Int{
    val colorInt = getColorRes(color)
    return colorInt and 0x00ffffff or ((alpha * 255.0f + 0.5f).toInt() shl 24)
}

/**
 * 从资源中获取Drawable
 */
fun Context.getDrawableRes(@DrawableRes drawable:Int) = ContextCompat.getDrawable(this,drawable)

/**
 * 尝试获取Context所属的activity
 */
fun Context.activity(): Activity? = when (this) {
    is Activity -> this
    else -> (this as? ContextWrapper)?.baseContext?.activity()
}

/**
 * 获取arrays.xml中定义的drawable资源数组
 */
fun Context.getDrawableArray(@ArrayRes drawableArrayRes : Int) : Array<Int?>{
    val typedArray = resources.obtainTypedArray(drawableArrayRes)

    val drawableArray = arrayOfNulls<Int>(typedArray.length())
    for(i in 0 until typedArray.length()){
        drawableArray[i] = typedArray.getResourceId(i,0)
    }
    typedArray.recycle()

    return drawableArray
}

fun Context.makeCall(number: String): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.sendSMSToNumber(number: String, text: String): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$number"))
        intent.putExtra("sms_body", text)
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.sendSMS(text: String): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:"))
        intent.putExtra("sms_body", text)
        startActivity(intent)
        true
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}

fun Context.browse(url: String, newTask: Boolean = false): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        if (newTask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
        true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        false
    }
}
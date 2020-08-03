package com.self.lib.extension

import android.content.ClipData
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.Html
import android.text.SpannableStringBuilder
import splitties.systemservices.clipboardManager

/**
 *
 * @author MBP
 * @date 2020/7/27
 */

fun CharSequence.toEditable() = SpannableStringBuilder(this)

fun CharSequence.toHtml():CharSequence = if (VERSION.SDK_INT >= VERSION_CODES.N) {
    Html.fromHtml(this.toString(), Html.FROM_HTML_MODE_LEGACY)
} else {
    @Suppress("DEPRECATION")
    Html.fromHtml(this.toString())
}

fun CharSequence.encryptPhone() : String?{
    return if (isEmpty() || !matches("\\d+".toRegex())) {
        this.toString()
    } else this.replace("(\\d{3})\\d+(\\d{4})".toRegex(), "$1****$2")
}

/**
 * 复制到剪贴板
 * @receiver CharSequence
 */
fun CharSequence.copyToClipBoard(){
    clipboardManager.setPrimaryClip(ClipData.newPlainText("text",this))
}
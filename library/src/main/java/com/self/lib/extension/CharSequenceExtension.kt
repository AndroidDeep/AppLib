package com.self.lib.extension

import android.content.ClipData
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.text.Html
import android.text.SpannableStringBuilder
import android.util.Base64
import okhttp3.internal.and
import splitties.systemservices.clipboardManager
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

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

/**
 * 字符串Base64编码
 * @receiver CharSequence  目标字符串
 * @return String?   编码字符串
 */
fun CharSequence.encodeBase64():String?{
    var base64:String? = null
    try {
        val data = this.toString().toByteArray(charset("UTF-8"))
        base64 = Base64.encodeToString(data, Base64.DEFAULT)
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return base64
}

/**
 * 字符串Base64解码
 * @receiver CharSequence
 * @return String?
 */
fun CharSequence.decodeBase64():String?{
    var data:String? = null
    try {
        val base64ByteArray = this.toString().toByteArray(UTF_8)
        data = String(Base64.decode(base64ByteArray, Base64.DEFAULT))
    } catch (e: RuntimeException) {
        e.printStackTrace()
    }
    return data
}

private val HEX_DIGITS_UPPER =
    charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
private val HEX_DIGITS_LOWER =
    charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

/**
 * 字符串MD5加密
 * @receiver CharSequence     要加密的字符串
 * @param isUpperCase Boolean   是否转为大写字母的MD5
 * @return String?          转化MD5字符串结果
 */
fun CharSequence.md5(isUpperCase:Boolean):String?{
    var md5Result:String? = null
    try {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(this.toString().toByteArray(UTF_8))

        val hexDigits = if (isUpperCase) HEX_DIGITS_UPPER else HEX_DIGITS_LOWER
        val ret = StringBuilder(bytes.size * 2)
        for (i in bytes.indices) {
            ret.append(hexDigits[bytes[i].toInt() shr 4 and 0x0f])
            ret.append(hexDigits[bytes[i] and 0x0f])
        }
        md5Result = ret.toString()
    }catch (e:RuntimeException){
        e.printStackTrace()
    }
    return md5Result
}
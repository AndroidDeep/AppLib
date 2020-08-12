package com.self.lib.extension

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

private var defaultTag: String? = null
private val LINE_SEPARATOR = System.getProperty("line.separator")

fun Any.log(info: Any?, tag: String? = null) {
    Log.v(tag ?: defaultTag ?: this.javaClass.simpleName, info.toString())
}

fun Any.logDebug(info: Any?, tag: String? = null) {
    Log.d(tag ?: defaultTag ?: this.javaClass.simpleName, info.toString())
}

fun Any.logInfo(info: Any?, tag: String? = null) {
    Log.i(tag ?: defaultTag ?: this.javaClass.simpleName, info.toString())
}

fun Any.logWarn(info: Any?, tag: String? = null) {
    Log.w(tag ?: defaultTag ?: this.javaClass.simpleName, info.toString())
}

fun Any.logError(info: Any?, tag: String? = null) {
    Log.e(tag ?: defaultTag ?: this.javaClass.simpleName, info.toString())
}

fun Any.logWtf(info: Any?, tag: String? = null) {
    Log.wtf(tag ?: defaultTag ?: this.javaClass.simpleName, info.toString())
}

fun Any.logJson(json: String, tag: String? = null) {
    var message: String
    message = try {
        when {
            json.startsWith("{") -> {
                val jsonObject = JSONObject(json)
                jsonObject.toString(4)
            }
            json.startsWith("[") -> {
                val jsonArray = JSONArray(json)
                jsonArray.toString(4)
            }
            else -> {
                json
            }
        }
    } catch (e: JSONException) {
        json
    }
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    message = LINE_SEPARATOR + message
    val lines = message.split(LINE_SEPARATOR!!.toRegex()).toTypedArray()
    val sb = StringBuilder()
    for (line in lines) {
        sb.append(line).append("\r\n")
    }
    Log.i(tag ?: defaultTag ?: this.javaClass.simpleName, sb.toString())
}

object Log{

    @JvmStatic fun setDefaultTag(tag:String){
        defaultTag = tag
    }

}
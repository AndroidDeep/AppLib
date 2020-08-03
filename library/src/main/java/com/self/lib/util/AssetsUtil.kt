package com.self.lib.util

import com.google.gson.JsonSyntaxException
import splitties.init.appCtx

object AssetsUtil {

    @JvmStatic fun getAssetsString(fileName: String): String? {

        return try {
            val stream = appCtx.assets.open(fileName)
            val size = stream.available()

            // Read the entire asset into a local byte buffer.
            val buffer = ByteArray(size)
            stream.read(buffer)
            stream.close()

            // Convert the buffer into a string.
            String(buffer, charset("UTF-8"))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @Throws(JsonSyntaxException::class)
    @JvmStatic fun <T> fromAssetsJson(fileName: String, classOfT: Class<T>): T? {
        return getAssetsString(fileName)?.let {
            GsonUtil.parseJson(it, classOfT)
        }
    }

}

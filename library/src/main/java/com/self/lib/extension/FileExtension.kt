package com.self.lib.extension

import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Locale

/**
 * 创建新文件，若存在同名文件，删除旧文件
 * @receiver File
 * @return Boolean
 */
fun File.createByDeleteOld() : Boolean{
    // file exists and unsuccessfully delete then return false
    if (exists() && !delete()) return false
    return if (parentFile?.createOrExistsDir() != true) false else try {
        createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

/**
 * 目录是否存在，不存在则创建
 * @receiver File
 * @return Boolean
 */
fun File.createOrExistsDir() = if (exists()) isDirectory else mkdirs()

/**
 * 获取文件MD5
 * @receiver File    文件
 * @param isUpperCase Boolean   是否转大写
 * @return String?   MD5结果
 */
fun File.getMd5(isUpperCase:Boolean = false):String?{
    return try {
        val buffer = ByteArray(1024)
        var len: Int
        val digest = MessageDigest.getInstance("MD5")
        val `in` = FileInputStream(this)
        while (`in`.read(buffer).also { len = it } != -1) {
            digest.update(buffer, 0, len)
        }
        `in`.close()
        val bigInt = BigInteger(1, digest.digest())
        bigInt.toString(16).run {
            if(isUpperCase) this.toUpperCase(Locale.getDefault())
            else this
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
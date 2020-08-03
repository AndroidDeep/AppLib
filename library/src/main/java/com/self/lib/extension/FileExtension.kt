package com.self.lib.extension

import java.io.File
import java.io.IOException

/**
 * File拓展方法
 * @author MBP
 * @date 2020/7/31
 */

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
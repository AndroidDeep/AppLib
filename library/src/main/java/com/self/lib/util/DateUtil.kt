package com.self.lib.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {

    @JvmStatic
    fun getToday(): String {
        //需要得到的格式
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

}
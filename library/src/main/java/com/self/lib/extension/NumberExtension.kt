package com.self.lib.extension

import java.math.RoundingMode.HALF_EVEN
import java.text.DecimalFormat

fun Number.toDecimal2(): String {
    val df = DecimalFormat("0.00")
    df.roundingMode = HALF_EVEN
    return df.format(this)
}

fun Number.toDecimal1(): String {
    val df = DecimalFormat("0.0")
    df.roundingMode = HALF_EVEN
    return df.format(this)
}

fun Number.toDecimal(countOfDecimal:Int): String {
    if(countOfDecimal < 0 ) return toString()
    var pattern = "0"
    if(countOfDecimal > 0){
        pattern = pattern.plus(".")
        repeat(countOfDecimal){
            pattern = pattern.plus("0")
        }
    }
    val df = DecimalFormat(pattern)
    df.roundingMode = HALF_EVEN
    return df.format(this)
}
package iti.student.finalproject.utils

import kotlin.math.pow
import kotlin.math.roundToInt

object NumberUtils {
    fun roundTo(value: Float, decimals: Int): Float {
        val factor = 10.0.pow(decimals).toFloat()
        return (value * factor).roundToInt() / factor
    }
}
package iti.student.finalproject.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
object TimeUtils {
    fun getDayMonth(date: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("d MMM", Locale.ENGLISH)

        val dateTime = LocalDateTime.parse(date, inputFormatter)
        return dateTime.format(outputFormatter)
    }

    fun getTime(date: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("h a", Locale.ENGLISH)

        val dateTime = LocalDateTime.parse(date, inputFormatter)
        return dateTime.format(outputFormatter)
    }

    fun getDayName(date: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val outputFormatter = DateTimeFormatter.ofPattern("EEE", Locale.ENGLISH)

        val dateTime = LocalDateTime.parse(date, inputFormatter)
        return dateTime.format(outputFormatter)
    }
}
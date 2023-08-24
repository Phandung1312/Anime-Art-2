package com.anime.art.ai.common.extension

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.abs

fun String.convertToShortDate(): String {
    val longDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val shortDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val date = try {
        longDateFormat.parse(this)
    } catch (e: Exception) {
        return ""
    }

    return shortDateFormat.format(date)
}

fun String.dayBetween(dateStr2: String): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")


    val date1 = LocalDate.parse(this, formatter)
    val date2 = LocalDate.parse(dateStr2, formatter)


    return  abs(ChronoUnit.DAYS.between(date1, date2))

}

fun String.compareWithFiveMinutes(dateStr2: String): Long {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    val dateTime1 = LocalDateTime.parse(this, formatter)
    val dateTime2 = LocalDateTime.parse(dateStr2, formatter)

    val minutesDifference = abs(ChronoUnit.MINUTES.between(dateTime1, dateTime2))

    return when {
        minutesDifference == 2L -> 1
        minutesDifference < 2L -> 0
        else -> 2
    }
}
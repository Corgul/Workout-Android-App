package com.example.workout_log.domain.util.extensions

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun LocalDate.formatDate(): String {
    val formatter = DateTimeFormatter.ofPattern("E',' LLLL d'${getDayOfMonthSuffix(this.dayOfMonth)}'")
    return formatter.format(this)
}

fun getDayOfMonthSuffix(day: Int): String {
    return if (day in 11..13) {
        "th"
    } else when (day % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}
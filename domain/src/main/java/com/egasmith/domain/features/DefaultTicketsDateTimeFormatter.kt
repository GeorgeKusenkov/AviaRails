package com.egasmith.domain.features

import java.text.SimpleDateFormat
import java.util.Locale

class DefaultTicketsDateTimeFormatter : TicketsDateTimeFormatter {
    private val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    private val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun formatDateTime(dateTime: String): String {
        val date = dateTimeFormat.parse(dateTime)
        return outputFormat.format(date)
    }

    override fun calculateTimeDifference(departure: String, arrival: String): Int {
        val departureDate = dateTimeFormat.parse(departure)
        val arrivalDate = dateTimeFormat.parse(arrival)
        val diffInMillis = arrivalDate.time - departureDate.time
        return kotlin.math.ceil(diffInMillis / (1000.0 * 60 * 60)).toInt()
    }
}

fun Int.formatHours(): String {
    return when {
        this % 100 in 11..19 -> "$this часов"
        this % 10 == 1 -> "$this час"
        this % 10 in 2..4 -> "$this часа"
        else -> "$this часов"
    }
}
package com.egasmith.domain.features

interface TicketsDateTimeFormatter {
    fun formatDateTime(dateTime: String): String
    fun calculateTimeDifference(departure: String, arrival: String): Int
}
package com.egasmith.domain.models.uiticket

data class UiTicket(
    val id: Int,
    val badge: String? = null,
    val price: String,
    val company: String,
    val departureDate: String,
    val departureAirport: String,
    val arrivalDate: String,
    val arrivalAirport: String,
    val hasTransfer: Boolean,
    val flyingTime: String
)
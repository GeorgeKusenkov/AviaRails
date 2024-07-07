package com.egasmith.domain.models.ticket

import com.egasmith.domain.models.Price

data class Ticket(
    val id: Int,
    val badge: String? = null,
    val price: Price,
    val providerName: String,
    val company: String,
    val departure: FlightDetails,
    val arrival: FlightDetails,
    val hasTransfer: Boolean,
    val hasVisaTransfer: Boolean,
    val luggage: Luggage?,
    val handLuggage: HandLuggage,
    val isReturnable: Boolean,
    val isExchangeable: Boolean
)
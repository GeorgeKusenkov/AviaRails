package com.egasmith.domain.models.ticketoffers

data class TicketOffers(
    val id: Int,
    val title: String,
    val timeRange: String,
    val price: String,
    val color: Int = 0
)
package com.egasmith.domain.models.ticketoffers

import com.egasmith.domain.models.Price

data class TicketOffers(
    val id: Int,
    val title: String,
    val timeRange: List<String>,
    val price: Price
)
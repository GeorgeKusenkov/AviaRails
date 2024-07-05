package com.egasmith.api.models.ticketoffers

import com.egasmith.api.models.Price
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketOffers(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("time_range") val timeRange: List<String>,
    @SerialName("price") val price: Price
)
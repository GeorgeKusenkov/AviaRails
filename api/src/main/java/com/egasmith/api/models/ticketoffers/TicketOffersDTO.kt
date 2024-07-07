package com.egasmith.api.models.ticketoffers

import com.egasmith.api.models.PriceDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketOffersDTO(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("time_range") val timeRange: List<String>,
    @SerialName("price") val priceDTO: PriceDTO
)
package com.egasmith.api.models.ticketoffers

import com.egasmith.api.models.PriceDTO
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TicketOffersDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("time_range") val timeRange: List<String>,
    @SerializedName("price") val price: PriceDTO
)
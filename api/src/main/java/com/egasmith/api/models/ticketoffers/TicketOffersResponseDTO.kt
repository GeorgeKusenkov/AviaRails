package com.egasmith.api.models.ticketoffers

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TicketOffersResponseDTO(
    @SerializedName("tickets_offers") val ticketsOffers: List<TicketOffersDTO>
)
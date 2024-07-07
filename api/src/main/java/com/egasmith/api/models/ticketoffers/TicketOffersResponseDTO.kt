package com.egasmith.api.models.ticketoffers

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketOffersResponseDTO(
    @SerialName("tickets_offers") val ticketsOffers: List<TicketOffersDTO>
)
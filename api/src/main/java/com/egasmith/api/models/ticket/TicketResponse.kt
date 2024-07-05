package com.egasmith.api.models.ticket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketResponse(
    @SerialName("tickets") val tickets: List<Ticket>
)
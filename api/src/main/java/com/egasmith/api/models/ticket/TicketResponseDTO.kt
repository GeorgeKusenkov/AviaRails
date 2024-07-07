package com.egasmith.api.models.ticket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketResponseDTO(
    @SerialName("tickets") val ticketDTOS: List<TicketDTO>
)
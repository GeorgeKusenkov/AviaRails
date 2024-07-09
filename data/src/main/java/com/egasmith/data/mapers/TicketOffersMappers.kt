package com.egasmith.data.mapers

import com.egasmith.api.models.ticketoffers.TicketOffersDTO
import com.egasmith.api.models.ticketoffers.TicketOffersResponseDTO
import com.egasmith.domain.models.ticketoffers.TicketOffersResponse
import com.egasmith.domain.models.ticketoffers.TicketOffers

fun TicketOffersResponseDTO.toDomain(): TicketOffersResponse {
    return TicketOffersResponse(
        ticketsOffers = ticketsOffers?.map { it.toDomain() } ?: emptyList()
    )
}

fun TicketOffersDTO.toDomain(): TicketOffers {
    return TicketOffers(
        id = id ?: 666,
        title = title ?: "",
        timeRange = timeRange?.joinToString(" ") ?: "",
        price = "${price?.value ?: "0"} '20BD'",
        color = 0
    )
}
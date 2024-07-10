package com.egasmith.data.mapers


import com.egasmith.api.models.ticket.FlightDetailsDTO
import com.egasmith.api.models.ticket.TicketDTO
import com.egasmith.api.models.ticket.TicketResponseDTO
import com.egasmith.domain.models.ticket.FlightDetails
import com.egasmith.domain.models.ticket.Ticket
import com.egasmith.domain.models.ticket.TicketResponse

fun TicketResponseDTO.toDomain(): TicketResponse {
    return TicketResponse(
        ticket = this.ticketDTOS.map { it.toDomain() }
    )
}

fun TicketDTO.toDomain(): Ticket {
    return Ticket(
        id = id,
        badge = badge,
        price = "${priceDTO.value} ₽",
        company = company,
        departure = departure.toDomain(),
        arrival = arrival.toDomain(),
        hasTransfer = hasTransfer
    )
}

fun FlightDetailsDTO.toDomain(): FlightDetails {
    return FlightDetails(
        town = town,
        date = date,
        airport = airport
    )
}
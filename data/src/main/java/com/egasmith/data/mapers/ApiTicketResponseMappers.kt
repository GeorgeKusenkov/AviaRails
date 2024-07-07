package com.egasmith.data.mapers


import com.egasmith.api.models.PriceDTO
import com.egasmith.api.models.ticket.FlightDetailsDTO
import com.egasmith.api.models.ticket.HandLuggageDTO
import com.egasmith.api.models.ticket.LuggageDTO
import com.egasmith.api.models.ticket.TicketDTO
import com.egasmith.api.models.ticket.TicketResponseDTO
import com.egasmith.domain.models.Price
import com.egasmith.domain.models.ticket.FlightDetails
import com.egasmith.domain.models.ticket.HandLuggage
import com.egasmith.domain.models.ticket.Luggage
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
        price = priceDTO.toDomain(),
        providerName = providerName,
        company = company,
        departure = departure.toDomain(),
        arrival = arrival.toDomain(),
        hasTransfer = hasTransfer,
        hasVisaTransfer = hasVisaTransfer,
        luggage = luggageDTO?.toDomain(),
        handLuggage = handLuggageDTO.toDomain(),
        isReturnable = isReturnable,
        isExchangeable = isExchangeable
    )
}

fun PriceDTO.toDomain(): Price {
    return Price(
        value = value
    )
}

fun FlightDetailsDTO.toDomain(): FlightDetails {
    return FlightDetails(
        town = town,
        date = date,
        airport = airport
    )
}

fun LuggageDTO.toDomain(): Luggage {
    return Luggage(
        hasLuggage = hasLuggage,
        price = priceDTO?.toDomain()
    )
}

fun HandLuggageDTO.toDomain(): HandLuggage {
    return HandLuggage(
        hasHandLuggage = hasHandLuggage,
        size = size
    )
}
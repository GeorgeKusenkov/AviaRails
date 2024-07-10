package com.egasmith.domain.usecases

import com.egasmith.domain.ApiResult
import com.egasmith.domain.TicketRepository
import com.egasmith.domain.features.TicketsDateTimeFormatter
import com.egasmith.domain.features.formatHours
import com.egasmith.domain.models.ticket.Ticket
import com.egasmith.domain.models.uiticket.UiTicket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTicketsUseCase @Inject constructor(
    private val ticketsRepository: TicketRepository,
    private val ticketsDateTimeFormatter: TicketsDateTimeFormatter
) {
    suspend operator fun invoke(): Flow<ApiResult<List<UiTicket>>> {
        return ticketsRepository.getTickets().map { apiResult ->
            apiResult.fold(
                onSuccess = { ticketResponse ->
                    val uiTickets = ticketResponse.ticket.map { ticket ->
                        mapToUiTicket(ticket)
                    }
                    ApiResult.Success(uiTickets)
                },
                onFailure = { exception ->
                    ApiResult.Failure(exception)
                }
            )
        }
    }

    private fun mapToUiTicket(ticket: Ticket): UiTicket {
        return UiTicket(
            id = ticket.id,
            badge = ticket.badge ?: "",
            price = ticket.price,
            company = ticket.company,
            departureDate = ticketsDateTimeFormatter.formatDateTime(ticket.departure.date),
            departureAirport = ticket.departure.airport,
            arrivalDate = ticketsDateTimeFormatter.formatDateTime(ticket.arrival.date),
            arrivalAirport = ticket.arrival.airport,
            hasTransfer = ticket.hasTransfer,
            flyingTime = getFlyingTime(ticket.departure.date, ticket.arrival.date)
        )
    }

    private fun getFlyingTime(departure: String, arrival: String): String {
        val difference = ticketsDateTimeFormatter.calculateTimeDifference(departure, arrival)
        return difference.formatHours()
    }

}
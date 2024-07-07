package com.egasmith.domain.usecases

import com.egasmith.domain.ApiResult
import com.egasmith.domain.TicketRepository
import com.egasmith.domain.models.ticket.TicketResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTicketsUseCase  @Inject constructor(private val ticketsRepository: TicketRepository) {
    suspend operator fun invoke(): Flow<ApiResult<TicketResponse>> = ticketsRepository.getTickets()
}
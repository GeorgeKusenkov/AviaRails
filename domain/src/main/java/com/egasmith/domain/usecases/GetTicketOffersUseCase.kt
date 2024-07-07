package com.egasmith.domain.usecases

import com.egasmith.domain.ApiResult
import com.egasmith.domain.TicketRepository
import com.egasmith.domain.models.ticketoffers.TicketOffersResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTicketOffersUseCase  @Inject constructor(private val ticketsRepository: TicketRepository) {
    suspend operator fun invoke(): Flow<ApiResult<TicketOffersResponse>> = ticketsRepository.getTicketOffers()
}
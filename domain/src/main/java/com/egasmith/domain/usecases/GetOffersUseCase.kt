package com.egasmith.domain.usecases

import com.egasmith.domain.ApiResult
import com.egasmith.domain.TicketRepository
import com.egasmith.domain.models.offer.OfferResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOffersUseCase @Inject constructor(private val ticketRepository: TicketRepository) {
    suspend operator fun invoke(): Flow<ApiResult<OfferResponse>> = ticketRepository.getOffers()
}
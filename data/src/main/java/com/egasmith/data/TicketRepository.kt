package com.egasmith.data

import com.egasmith.api.AviaRailsApi
import com.egasmith.data.mapers.toDomain
import com.egasmith.domain.ApiResult
import com.egasmith.domain.TicketRepository
import com.egasmith.domain.models.offer.OfferResponse
import com.egasmith.domain.models.ticket.TicketResponse
import com.egasmith.domain.models.ticketoffers.TicketOffersResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketRepositoryImpl @Inject constructor(
    private val aviaRailsApi: AviaRailsApi
) : TicketRepository {
    override suspend fun getTickets(): Flow<ApiResult<TicketResponse>> = flow {
        val apiResult = ApiResult.safeApiCall { aviaRailsApi.getTickets() }
        val domainResult = when (apiResult) {
            is ApiResult.Success -> ApiResult.Success(apiResult.data.toDomain())
            is ApiResult.Failure -> apiResult
        }
        emit(domainResult)
    }

    override suspend fun getTicketOffers(): Flow<ApiResult<TicketOffersResponse>> = flow {
        val apiResult = ApiResult.safeApiCall { aviaRailsApi.getTicketOffers() }
        val domainResult = when (apiResult) {
            is ApiResult.Success -> ApiResult.Success(apiResult.data.toDomain())
            is ApiResult.Failure -> apiResult
        }
        emit(domainResult)
    }

    override suspend fun getOffers(): Flow<ApiResult<OfferResponse>> = flow {
        val apiResult = ApiResult.safeApiCall { aviaRailsApi.getOffers() }
        val domainResult = when (apiResult) {
            is ApiResult.Success -> ApiResult.Success(apiResult.data.toDomain())
            is ApiResult.Failure -> apiResult
        }
        emit(domainResult)
    }
}
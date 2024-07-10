package com.egasmith.domain

import com.egasmith.domain.models.offer.OfferResponse
import com.egasmith.domain.models.ticket.TicketResponse
import com.egasmith.domain.models.ticketoffers.TicketOffersResponse
import kotlinx.coroutines.flow.Flow

interface TicketRepository {
    suspend fun getTickets(): Flow<ApiResult<TicketResponse>>
    suspend fun getTicketOffers(): Flow<ApiResult<TicketOffersResponse>>
    suspend fun getOffers(): Flow<ApiResult<OfferResponse>>
}

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Failure(val exception: Exception) : ApiResult<Nothing>()

    inline fun <R> fold(
        onSuccess: (T) -> R,
        onFailure: (Exception) -> R
    ): R = when (this) {
        is Success -> onSuccess(data)
        is Failure -> onFailure(exception)
    }

    companion object {
        inline fun <T> safeApiCall(apiCall: () -> T): ApiResult<T> {
            return try {
                Success(apiCall())
            } catch (e: Exception) {
                Failure(e)
            }
        }
    }
}
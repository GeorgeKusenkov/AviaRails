package com.egasmith.api.models.ticket

import com.egasmith.api.models.PriceDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketDTO(
    @SerialName("id") val id: Int,
    @SerialName("badge") val badge: String? = null,
    @SerialName("price") val priceDTO: PriceDTO,
    @SerialName("provider_name") val providerName: String,
    @SerialName("company") val company: String,
    @SerialName("departure") val departure: FlightDetailsDTO,
    @SerialName("arrival") val arrival: FlightDetailsDTO,
    @SerialName("has_transfer") val hasTransfer: Boolean,
    @SerialName("has_visa_transfer") val hasVisaTransfer: Boolean,
    @SerialName("luggage") val luggageDTO: LuggageDTO?,
    @SerialName("hand_luggage") val handLuggageDTO: HandLuggageDTO,
    @SerialName("is_returnable") val isReturnable: Boolean,
    @SerialName("is_exchangable") val isExchangeable: Boolean
)
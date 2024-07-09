package com.egasmith.api.models.ticket

import com.egasmith.api.models.PriceDTO
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TicketDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("badge") val badge: String? = null,
    @SerializedName("price") val priceDTO: PriceDTO,
    @SerializedName("provider_name") val providerName: String,
    @SerializedName("company") val company: String,
    @SerializedName("departure") val departure: FlightDetailsDTO,
    @SerializedName("arrival") val arrival: FlightDetailsDTO,
    @SerializedName("has_transfer") val hasTransfer: Boolean,
    @SerializedName("has_visa_transfer") val hasVisaTransfer: Boolean,
    @SerializedName("luggage") val luggageDTO: LuggageDTO?,
    @SerializedName("hand_luggage") val handLuggageDTO: HandLuggageDTO,
    @SerializedName("is_returnable") val isReturnable: Boolean,
    @SerializedName("is_exchangable") val isExchangeable: Boolean
)
package com.egasmith.api.models.ticket

import com.egasmith.api.models.Price
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    @SerialName("id") val id: Int,
    @SerialName("badge") val badge: String? = null,
    @SerialName("price") val price: Price,
    @SerialName("provider_name") val providerName: String,
    @SerialName("company") val company: String,
    @SerialName("departure") val departure: FlightDetails,
    @SerialName("arrival") val arrival: FlightDetails,
    @SerialName("has_transfer") val hasTransfer: Boolean,
    @SerialName("has_visa_transfer") val hasVisaTransfer: Boolean,
    @SerialName("luggage") val luggage: Luggage?,
    @SerialName("hand_luggage") val handLuggage: HandLuggage,
    @SerialName("is_returnable") val isReturnable: Boolean,
    @SerialName("is_exchangable") val isExchangeable: Boolean
)
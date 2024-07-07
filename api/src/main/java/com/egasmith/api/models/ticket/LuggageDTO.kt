package com.egasmith.api.models.ticket

import com.egasmith.api.models.PriceDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LuggageDTO(
    @SerialName("has_luggage") val hasLuggage: Boolean,
    @SerialName("price") val priceDTO: PriceDTO?
)
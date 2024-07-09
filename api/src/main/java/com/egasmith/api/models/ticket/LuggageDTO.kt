package com.egasmith.api.models.ticket

import com.egasmith.api.models.PriceDTO
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class LuggageDTO(
    @SerializedName("has_luggage") val hasLuggage: Boolean,
    @SerializedName("price") val priceDTO: PriceDTO?
)
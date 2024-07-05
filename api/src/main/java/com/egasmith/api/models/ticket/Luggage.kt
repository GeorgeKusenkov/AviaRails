package com.egasmith.api.models.ticket

import com.egasmith.api.models.Price
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Luggage(
    @SerialName("has_luggage") val hasLuggage: Boolean,
    @SerialName("price") val price: Price?
)
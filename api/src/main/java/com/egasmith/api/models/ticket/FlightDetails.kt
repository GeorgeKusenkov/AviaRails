package com.egasmith.api.models.ticket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FlightDetails(
    @SerialName("town") val town: String,
    @SerialName("date") val date: String,
    @SerialName("airport") val airport: String
)
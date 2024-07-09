package com.egasmith.api.models.ticket

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class FlightDetailsDTO(
    @SerializedName("town") val town: String,
    @SerializedName("date") val date: String,
    @SerializedName("airport") val airport: String
)
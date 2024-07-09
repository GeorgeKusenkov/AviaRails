package com.egasmith.api.models.ticket

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class HandLuggageDTO(
    @SerializedName("has_hand_luggage") val hasHandLuggage: Boolean,
    @SerializedName("size") val size: String?
)
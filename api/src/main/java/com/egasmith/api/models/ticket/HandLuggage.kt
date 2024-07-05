package com.egasmith.api.models.ticket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HandLuggage(
    @SerialName("has_hand_luggage") val hasHandLuggage: Boolean,
    @SerialName("size") val size: String?
)
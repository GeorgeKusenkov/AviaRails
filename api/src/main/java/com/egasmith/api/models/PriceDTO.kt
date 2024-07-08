package com.egasmith.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceDTO(
    @SerialName("value") val value: Int
)
package com.egasmith.api.models

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PriceDTO(
    @SerializedName("value") val value: Int
)
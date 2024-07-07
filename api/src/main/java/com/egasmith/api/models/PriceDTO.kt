package com.egasmith.api.models

import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PriceDTO(
    @SerialName("value") val value: Int
)  {
    init {
        Log.d("PriceDTO", "Price loaded: value=$value")
    }
}
package com.egasmith.api.models.offer

import com.egasmith.api.models.Price
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Offer(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("town") val town: String,
    @SerialName("price") val price: Price
)
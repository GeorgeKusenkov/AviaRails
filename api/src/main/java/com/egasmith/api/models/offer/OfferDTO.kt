package com.egasmith.api.models.offer

import com.egasmith.api.models.PriceDTO
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OfferDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("town") val town: String,
    @SerializedName("price") val price: PriceDTO
)
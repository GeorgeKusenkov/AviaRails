package com.egasmith.api.models.offer

import android.util.Log
import com.egasmith.api.models.PriceDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferDTO(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("town") val town: String,
    @SerialName("price") val price: PriceDTO
) {
    init {
        Log.d("OfferDTO", "Offer loaded: id=$id, title=$title, town=$town, price=${price.value}")
    }
}
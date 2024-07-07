package com.egasmith.api.models.offer

import android.util.Log
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferResponseDTO(
    @SerialName("offers") val offers: List<OfferDTO>
) {
    init {
        Log.d("OfferResponseDTO", "Offers loaded: $offers")
    }
}
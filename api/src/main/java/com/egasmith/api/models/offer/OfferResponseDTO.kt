package com.egasmith.api.models.offer

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OfferResponseDTO(
    @SerializedName("offers") val offers: List<OfferDTO>
)
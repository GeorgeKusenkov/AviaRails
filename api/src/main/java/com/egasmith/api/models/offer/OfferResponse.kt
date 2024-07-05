package com.egasmith.api.models.offer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OfferResponse(
    @SerialName("offers") val offers: List<Offer>
)
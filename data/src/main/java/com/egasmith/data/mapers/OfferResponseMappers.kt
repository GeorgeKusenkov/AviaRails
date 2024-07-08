package com.egasmith.data.mapers

import com.egasmith.api.models.offer.OfferDTO
import com.egasmith.api.models.offer.OfferResponseDTO
import com.egasmith.domain.models.offer.Offer
import com.egasmith.domain.models.offer.OfferResponse


fun OfferResponseDTO.toDomain(): OfferResponse {
    return OfferResponse(
        offers = this.offers.map { it.toDomain() }
    )
}

fun OfferDTO.toDomain(): Offer {
    return Offer(
        id = id,
        title = title,
        town = town,
        price = "от ${price.value} р.",
        image = null
    )
}
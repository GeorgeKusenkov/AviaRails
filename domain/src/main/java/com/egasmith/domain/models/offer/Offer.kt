package com.egasmith.domain.models.offer

import com.egasmith.domain.models.Price

data class Offer(
    val id: Int,
    val title: String,
    val town: String,
    val price: Price
)
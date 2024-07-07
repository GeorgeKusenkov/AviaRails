package com.egasmith.domain.models.ticket

import com.egasmith.domain.models.Price


data class Luggage(
    val hasLuggage: Boolean,
    val price: Price?
)
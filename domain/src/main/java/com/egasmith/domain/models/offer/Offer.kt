package com.egasmith.domain.models.offer

import android.widget.ImageView
import com.egasmith.domain.models.Price

data class Offer(
    val id: Int,
    val title: String,
    val town: String,
    val price: String,
    val image: Int?
)
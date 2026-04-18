package com.multibank.realtimepricetracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PriceUpdate(
    val symbol: String,
    val price: Double,
    val timestamp: Long
)
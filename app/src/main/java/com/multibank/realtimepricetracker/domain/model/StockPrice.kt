package com.multibank.realtimepricetracker.domain.model

data class StockPrice(
    val symbol: String,
    val currentPrice: Double,
    val previousPrice: Double?,
    val timestamp: Long
) {
    val isPriceUp: Boolean?
        get() = previousPrice?.let { currentPrice > it }

    val priceChange: Double?
        get() = previousPrice?.let { currentPrice - it }

    val priceChangePercentage: Double?
        get() = previousPrice?.let {
            if (it == 0.0) 0.0 else ((currentPrice - it) / it) * 100
        }
}
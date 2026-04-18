package com.multibank.realtimepricetracker.data.mapper

import com.multibank.realtimepricetracker.data.model.PriceUpdate
import com.multibank.realtimepricetracker.domain.model.StockPrice
import javax.inject.Inject

class PriceUpdateMapper @Inject constructor() {

    fun toDomain(
        priceUpdate: PriceUpdate,
        previousPrice: Double?
    ): StockPrice {
        return StockPrice(
            symbol = priceUpdate.symbol,
            currentPrice = priceUpdate.price,
            previousPrice = previousPrice,
            timestamp = priceUpdate.timestamp
        )
    }
}
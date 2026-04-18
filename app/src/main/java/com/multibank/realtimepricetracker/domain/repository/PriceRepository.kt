package com.multibank.realtimepricetracker.domain.repository

import com.multibank.realtimepricetracker.domain.model.ConnectionStatus
import com.multibank.realtimepricetracker.domain.model.StockPrice
import kotlinx.coroutines.flow.Flow

interface PriceRepository {
    fun observePrices(): Flow<List<StockPrice>>
    fun observePrice(symbol: String): Flow<StockPrice?>
    fun observeConnectionStatus(): Flow<ConnectionStatus>

    suspend fun startPriceFeed()
    suspend fun stopPriceFeed()
}
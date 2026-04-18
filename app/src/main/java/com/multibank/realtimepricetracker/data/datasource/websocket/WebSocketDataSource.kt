package com.multibank.realtimepricetracker.data.datasource.websocket

import com.multibank.realtimepricetracker.data.model.PriceUpdate
import kotlinx.coroutines.flow.Flow

interface WebSocketDataSource {
    fun events(): Flow<WebSocketEvent>

    suspend fun connect()
    suspend fun disconnect()
    suspend fun sendPriceUpdate(priceUpdate: PriceUpdate)

    fun isConnected(): Boolean
}
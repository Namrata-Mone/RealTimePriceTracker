package com.multibank.realtimepricetracker.data.datasource.websocket

import com.multibank.realtimepricetracker.data.model.PriceUpdate

sealed interface WebSocketEvent {
    data object Connected : WebSocketEvent
    data object Disconnected : WebSocketEvent
    data class PriceUpdateReceived(val priceUpdate: PriceUpdate) : WebSocketEvent
    data class Failure(val throwable: Throwable) : WebSocketEvent
}
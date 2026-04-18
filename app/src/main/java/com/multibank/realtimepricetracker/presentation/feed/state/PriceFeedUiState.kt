package com.multibank.realtimepricetracker.presentation.feed.state

import com.multibank.realtimepricetracker.domain.model.ConnectionStatus
import com.multibank.realtimepricetracker.domain.model.StockPrice

data class PriceFeedUiState(
    val prices: List<StockPrice> = emptyList(),
    val connectionStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED
)
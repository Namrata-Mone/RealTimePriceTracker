package com.multibank.realtimepricetracker.presentation.details.state

import com.multibank.realtimepricetracker.domain.model.StockPrice

data class SymbolDetailsUiState(
    val symbol: String = "",
    val stockPrice: StockPrice? = null,
    val description: String = ""
)
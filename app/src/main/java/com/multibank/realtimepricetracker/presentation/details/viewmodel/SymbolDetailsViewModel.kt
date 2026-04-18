package com.multibank.realtimepricetracker.presentation.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multibank.realtimepricetracker.core.common.StockMetadata
import com.multibank.realtimepricetracker.domain.model.StockPrice
import com.multibank.realtimepricetracker.domain.usecase.ObserveStockUseCase
import com.multibank.realtimepricetracker.presentation.details.state.SymbolDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SymbolDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeStockUseCase: ObserveStockUseCase
) : ViewModel() {

    private val symbol: String = checkNotNull(savedStateHandle["symbol"])

    val uiState: StateFlow<SymbolDetailsUiState> = observeStockUseCase(symbol)
        .map { stockPrice: StockPrice? ->
            SymbolDetailsUiState(
                symbol = symbol,
                stockPrice = stockPrice,
                description = StockMetadata.description(symbol)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SymbolDetailsUiState(
                symbol = symbol,
                description = StockMetadata.description(symbol)
            )
        )
}
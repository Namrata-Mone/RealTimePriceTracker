package com.multibank.realtimepricetracker.presentation.details.viewmodel

import com.multibank.realtimepricetracker.domain.usecase.EnsurePriceFeedStartedUseCase
import com.multibank.realtimepricetracker.domain.usecase.ObserveSymbolPriceUseCase
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multibank.realtimepricetracker.presentation.details.state.SymbolDetailsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SymbolDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeSymbolPrice: ObserveSymbolPriceUseCase,
    private val ensurePriceFeedStarted: EnsurePriceFeedStartedUseCase
) : ViewModel() {

    private val symbol: String = checkNotNull(savedStateHandle["symbol"])
    private var hasRequestedFeedStart = false

    private val _uiState = MutableStateFlow(
        SymbolDetailsUiState(
            symbol = symbol,
            description = StockMetadata.description(symbol)
        )
    )
    val uiState: StateFlow<SymbolDetailsUiState> = _uiState.asStateFlow()

    init {
        observeSymbol()
    }

    fun startPriceFeedIfNeeded() {
        if (hasRequestedFeedStart) return
        hasRequestedFeedStart = true

        viewModelScope.launch {
            ensurePriceFeedStarted()
        }
    }

    private fun observeSymbol() {
        viewModelScope.launch {
            observeSymbolPrice(symbol).collect { stockPrice ->
                _uiState.update { it.copy(stockPrice = stockPrice) }
            }
        }
    }
}
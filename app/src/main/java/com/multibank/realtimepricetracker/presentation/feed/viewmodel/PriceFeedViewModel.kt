package com.multibank.realtimepricetracker.presentation.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multibank.realtimepricetracker.domain.usecase.ObserveConnectionStatusUseCase
import com.multibank.realtimepricetracker.domain.usecase.ObservePriceUpdatesUseCase
import com.multibank.realtimepricetracker.domain.usecase.StartPriceFeedUseCase
import com.multibank.realtimepricetracker.domain.usecase.StopPriceFeedUseCase
import com.multibank.realtimepricetracker.presentation.feed.state.PriceFeedUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PriceFeedViewModel @Inject constructor(
    observePrices: ObservePriceUpdatesUseCase,
    observeConnectionStatus: ObserveConnectionStatusUseCase,
    private val startPriceFeed: StartPriceFeedUseCase,
    private val stopPriceFeed: StopPriceFeedUseCase
) : ViewModel() {

    val uiState: StateFlow<PriceFeedUiState> = combine(
        observePrices(),
        observeConnectionStatus()
    ) { prices, connectionStatus ->
        PriceFeedUiState(
            prices = prices,
            connectionStatus = connectionStatus
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PriceFeedUiState()
    )

    fun startConnection() {
        viewModelScope.launch {
            startPriceFeed()
        }
    }

    fun stopConnection() {
        viewModelScope.launch {
            stopPriceFeed()
        }
    }
}
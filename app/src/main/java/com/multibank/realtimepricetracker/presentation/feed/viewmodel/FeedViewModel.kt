package com.multibank.realtimepricetracker.presentation.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.multibank.realtimepricetracker.domain.model.ConnectionStatus
import com.multibank.realtimepricetracker.domain.model.StockPrice
import com.multibank.realtimepricetracker.domain.usecase.ObserveConnectionStatusUseCase
import com.multibank.realtimepricetracker.domain.usecase.ObservePriceUpdatesUseCase
import com.multibank.realtimepricetracker.domain.usecase.StartPriceFeedUseCase
import com.multibank.realtimepricetracker.domain.usecase.StopPriceFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val observePrices: ObservePriceUpdatesUseCase,
    private val observeConnectionStatus: ObserveConnectionStatusUseCase,
    private val startPriceFeed: StartPriceFeedUseCase,
    private val stopPriceFeed: StopPriceFeedUseCase
) : ViewModel() {

    val prices: StateFlow<List<StockPrice>> = observePrices()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val connectionStatus: StateFlow<ConnectionStatus> = observeConnectionStatus()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ConnectionStatus.DISCONNECTED)

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
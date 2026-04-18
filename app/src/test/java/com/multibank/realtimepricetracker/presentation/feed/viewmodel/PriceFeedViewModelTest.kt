package com.multibank.realtimepricetracker.presentation.feed.viewmodel

import app.cash.turbine.test
import com.multibank.realtimepricetracker.domain.model.ConnectionStatus
import com.multibank.realtimepricetracker.domain.model.StockPrice
import com.multibank.realtimepricetracker.domain.usecase.ObserveConnectionStatusUseCase
import com.multibank.realtimepricetracker.domain.usecase.ObservePriceUpdatesUseCase
import com.multibank.realtimepricetracker.domain.usecase.StartPriceFeedUseCase
import com.multibank.realtimepricetracker.domain.usecase.StopPriceFeedUseCase
import com.multibank.realtimepricetracker.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PriceFeedViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var observePrices: ObservePriceUpdatesUseCase
    private lateinit var observeConnectionStatus: ObserveConnectionStatusUseCase
    private lateinit var startPriceFeed: StartPriceFeedUseCase
    private lateinit var stopPriceFeed: StopPriceFeedUseCase

    private lateinit var pricesFlow: MutableStateFlow<List<StockPrice>>
    private lateinit var connectionStatusFlow: MutableStateFlow<ConnectionStatus>

    private lateinit var viewModel: PriceFeedViewModel

    @Before
    fun setup() {
        observePrices = mockk()
        observeConnectionStatus = mockk()
        startPriceFeed = mockk()
        stopPriceFeed = mockk()

        pricesFlow = MutableStateFlow(emptyList())
        connectionStatusFlow = MutableStateFlow(ConnectionStatus.DISCONNECTED)

        every { observePrices() } returns pricesFlow
        every { observeConnectionStatus() } returns connectionStatusFlow

        coEvery { startPriceFeed() } returns Unit
        coEvery { stopPriceFeed() } returns Unit

        viewModel = PriceFeedViewModel(
            observePrices = observePrices,
            observeConnectionStatus = observeConnectionStatus,
            startPriceFeed = startPriceFeed,
            stopPriceFeed = stopPriceFeed
        )
    }

    fun stock(
        symbol: String,
        price: Double,
        prev: Double,
        time: Long
    ) = StockPrice(symbol, price, prev, time)

    @Test
    fun `uiState should expose prices in highest price first order`() = runTest {
        val now = System.currentTimeMillis()

        val goog = stock("GOOG", 220.0, 210.0, now)
        val aapl = stock("AAPL", 150.0, 145.0, now)
        val tsla = stock("TSLA", 90.0, 95.0, now)

        viewModel.uiState.test {
            val initial = awaitItem()
            assertTrue(initial.prices.isEmpty())
            assertEquals(ConnectionStatus.DISCONNECTED, initial.connectionStatus)

            pricesFlow.value = listOf(goog, aapl, tsla)

            val updated = awaitItem()
            assertEquals(3, updated.prices.size)
            assertEquals("GOOG", updated.prices[0].symbol)
            assertEquals("AAPL", updated.prices[1].symbol)
            assertEquals("TSLA", updated.prices[2].symbol)
        }
    }

    @Test
    fun `uiState should update when connection status changes`() = runTest {
        viewModel.uiState.test {
            val initial = awaitItem()
            assertEquals(ConnectionStatus.DISCONNECTED, initial.connectionStatus)

            connectionStatusFlow.value = ConnectionStatus.CONNECTED

            val updated = awaitItem()
            assertEquals(ConnectionStatus.CONNECTED, updated.connectionStatus)
        }
    }

    @Test
    fun `startConnection should call StartPriceFeedUseCase`() = runTest {
        viewModel.startConnection()

        coVerify(exactly = 1) { startPriceFeed() }
    }

    @Test
    fun `stopConnection should call StopPriceFeedUseCase`() = runTest {
        viewModel.stopConnection()

        coVerify(exactly = 1) { stopPriceFeed() }
    }
}
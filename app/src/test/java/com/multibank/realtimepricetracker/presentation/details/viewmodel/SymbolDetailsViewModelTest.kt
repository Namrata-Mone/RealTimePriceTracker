package com.multibank.realtimepricetracker.presentation.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.multibank.realtimepricetracker.domain.model.StockPrice
import com.multibank.realtimepricetracker.domain.repository.PriceRepository
import com.multibank.realtimepricetracker.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SymbolDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var priceRepository: PriceRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var priceFlow: MutableStateFlow<StockPrice?>

    private lateinit var viewModel: SymbolDetailsViewModel

    @Before
    fun setup() {
        priceRepository = mockk(relaxed = true)
        savedStateHandle = SavedStateHandle(mapOf("symbol" to "AAPL"))
        priceFlow = MutableStateFlow(null)

        every { priceRepository.observePrice("AAPL") } returns priceFlow
        coEvery { priceRepository.startPriceFeed() } returns Unit

        viewModel = SymbolDetailsViewModel(
            savedStateHandle = savedStateHandle,
            priceRepository = priceRepository
        )
    }

    @Test
    fun `initial state should contain symbol description and null stock price`() = runTest {
        viewModel.uiState.test {
            val initial = awaitItem()

            assertEquals("AAPL", initial.symbol)
            assertNull(initial.stockPrice)
            assertEquals("Consumer electronics and software", initial.description)
        }
    }

    @Test
    fun `uiState should update when matching stock price is emitted`() = runTest {
        val now = System.currentTimeMillis()
        val updatedPrice = StockPrice(
            symbol = "AAPL",
            currentPrice = 182.45,
            previousPrice = 180.10,
            timestamp = now
        )

        viewModel.uiState.test {
            val initial = awaitItem()
            assertNull(initial.stockPrice)

            priceFlow.value = updatedPrice

            val updated = awaitItem()
            assertEquals("AAPL", updated.symbol)
            assertEquals(182.45, updated.stockPrice?.currentPrice ?: 0.0, 0.01)
            assertEquals(180.10, updated.stockPrice?.previousPrice ?: 0.0, 0.01)
            assertEquals(now, updated.stockPrice?.timestamp)
        }
    }

    @Test
    fun `startPriceFeedIfNeeded should call repository only once`() = runTest {
        viewModel.startPriceFeedIfNeeded()
        viewModel.startPriceFeedIfNeeded()

        coVerify(exactly = 1) { priceRepository.startPriceFeed() }
    }
}
package com.multibank.realtimepricetracker.data.repository

import com.multibank.realtimepricetracker.core.common.StockSymbols
import com.multibank.realtimepricetracker.data.datasource.websocket.WebSocketDataSource
import com.multibank.realtimepricetracker.data.datasource.websocket.WebSocketEvent
import com.multibank.realtimepricetracker.data.mapper.PriceUpdateMapper
import com.multibank.realtimepricetracker.data.model.PriceUpdate
import com.multibank.realtimepricetracker.domain.model.ConnectionStatus
import com.multibank.realtimepricetracker.domain.model.StockPrice
import com.multibank.realtimepricetracker.domain.repository.PriceRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class PriceRepositoryImpl @Inject constructor(
    private val webSocketDataSource: WebSocketDataSource,
    private val priceUpdateMapper: PriceUpdateMapper
) : PriceRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val stockPrices = MutableStateFlow<Map<String, StockPrice>>(emptyMap())
    private val connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)

    private var observeEventsJob: Job? = null
    private var priceFeedJob: Job? = null

    override fun observePrices(): Flow<List<StockPrice>> {
        return stockPrices.map { prices ->
            prices.values.sortedByDescending { it.currentPrice }
        }
    }

    override fun observePrice(symbol: String): Flow<StockPrice?> {
        return stockPrices
            .map { it[symbol] }
            .distinctUntilChanged()
    }

    override fun observeConnectionStatus(): Flow<ConnectionStatus> = connectionStatus

    override suspend fun startPriceFeed() {
        if (priceFeedJob?.isActive == true) return

        connectionStatus.value = ConnectionStatus.CONNECTING
        webSocketDataSource.connect()

        if (observeEventsJob?.isActive != true) {
            observeEventsJob = scope.launch {
                webSocketDataSource.events().collect { event ->
                    when (event) {
                        WebSocketEvent.Connected -> {
                            connectionStatus.value = ConnectionStatus.CONNECTED
                        }

                        WebSocketEvent.Disconnected -> {
                            connectionStatus.value = ConnectionStatus.DISCONNECTED
                        }

                        is WebSocketEvent.Failure -> {
                            connectionStatus.value = ConnectionStatus.ERROR
                        }

                        is WebSocketEvent.PriceUpdateReceived -> {
                            updateStockPrice(event.priceUpdate)
                        }
                    }
                }
            }
        }

        priceFeedJob = scope.launch {
            while (isActive) {
                StockSymbols.all.forEach { symbol ->
                    val priceUpdate = PriceUpdate(
                        symbol = symbol,
                        price = nextPriceFor(symbol),
                        timestamp = System.currentTimeMillis()
                    )
                    webSocketDataSource.sendPriceUpdate(priceUpdate)
                }
                delay(2_000)
            }
        }
    }

    override suspend fun stopPriceFeed() {
        priceFeedJob?.cancel()
        priceFeedJob = null
        webSocketDataSource.disconnect()
    }

    private fun updateStockPrice(priceUpdate: PriceUpdate) {
        stockPrices.update { currentPrices ->
            val previousPrice = currentPrices[priceUpdate.symbol]?.currentPrice

            val updatedPrice = priceUpdateMapper.toDomain(
                priceUpdate = priceUpdate,
                previousPrice = previousPrice
            )

            currentPrices + (priceUpdate.symbol to updatedPrice)
        }
    }

    private fun nextPriceFor(symbol: String): Double {
        val currentPrice = stockPrices.value[symbol]?.currentPrice
            ?: Random.nextDouble(100.0, 1000.0)

        val delta = Random.nextDouble(-15.0, 15.0)
        val updatedPrice = (currentPrice + delta).coerceAtLeast(1.0)

        return String.format("%.2f", updatedPrice).toDouble()
    }
}
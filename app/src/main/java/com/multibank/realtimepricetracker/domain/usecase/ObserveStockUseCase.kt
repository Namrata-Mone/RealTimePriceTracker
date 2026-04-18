package com.multibank.realtimepricetracker.domain.usecase

import com.multibank.realtimepricetracker.domain.model.StockPrice
import com.multibank.realtimepricetracker.domain.repository.PriceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveStockUseCase @Inject constructor(
    private val repository: PriceRepository
) {
    operator fun invoke(symbol: String): Flow<StockPrice?> {
        return repository.observePrice(symbol)
    }
}
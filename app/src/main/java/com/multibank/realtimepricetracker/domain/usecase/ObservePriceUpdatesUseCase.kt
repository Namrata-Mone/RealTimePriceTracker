package com.multibank.realtimepricetracker.domain.usecase

import com.multibank.realtimepricetracker.domain.model.StockPrice
import com.multibank.realtimepricetracker.domain.repository.PriceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePriceUpdatesUseCase @Inject constructor(
    private val repository: PriceRepository
) {
    operator fun invoke(): Flow<List<StockPrice>> {
        return repository.observePrices()
    }
}
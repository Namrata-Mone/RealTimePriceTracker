package com.multibank.realtimepricetracker.domain.usecase

import com.multibank.realtimepricetracker.domain.repository.PriceRepository
import javax.inject.Inject

class ObserveSymbolPriceUseCase @Inject constructor(
    private val repository: PriceRepository
) {
    operator fun invoke(symbol: String) = repository.observePrice(symbol)
}
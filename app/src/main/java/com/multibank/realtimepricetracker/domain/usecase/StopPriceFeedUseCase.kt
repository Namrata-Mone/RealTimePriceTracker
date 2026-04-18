package com.multibank.realtimepricetracker.domain.usecase

import com.multibank.realtimepricetracker.domain.repository.PriceRepository
import javax.inject.Inject

class StopPriceFeedUseCase @Inject constructor(
    private val repository: PriceRepository
) {
    suspend operator fun invoke() {
        repository.stopPriceFeed()
    }
}
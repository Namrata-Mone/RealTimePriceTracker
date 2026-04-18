package com.multibank.realtimepricetracker.domain.usecase

import com.multibank.realtimepricetracker.domain.repository.PriceRepository
import javax.inject.Inject

class StartPriceFeedUseCase @Inject constructor(
    private val repository: PriceRepository
) {
    suspend operator fun invoke() {
        repository.startPriceFeed()
    }
}
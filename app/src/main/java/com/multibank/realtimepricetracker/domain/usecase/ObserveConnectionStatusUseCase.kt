package com.multibank.realtimepricetracker.domain.usecase

import com.multibank.realtimepricetracker.domain.model.ConnectionStatus
import com.multibank.realtimepricetracker.domain.repository.PriceRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConnectionStatusUseCase @Inject constructor(
    private val repository: PriceRepository
) {
    operator fun invoke(): Flow<ConnectionStatus> {
        return repository.observeConnectionStatus()
    }
}
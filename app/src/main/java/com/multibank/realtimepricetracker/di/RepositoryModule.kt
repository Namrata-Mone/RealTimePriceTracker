package com.multibank.realtimepricetracker.di

import com.multibank.realtimepricetracker.data.datasource.websocket.WebSocketDataSource
import com.multibank.realtimepricetracker.data.datasource.websocket.WebSocketDataSourceImpl
import com.multibank.realtimepricetracker.data.repository.PriceRepositoryImpl
import com.multibank.realtimepricetracker.domain.repository.PriceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWebSocketDataSource(
        impl: WebSocketDataSourceImpl
    ): WebSocketDataSource

    @Binds
    @Singleton
    abstract fun bindPriceRepository(
        impl: PriceRepositoryImpl
    ): PriceRepository
}
package com.multibank.realtimepricetracker.presentation.navigation

object NavRoutes {
    const val FEED = "feed"
    const val DETAILS = "details/{symbol}"

    fun details(symbol: String) = "details/$symbol"
}
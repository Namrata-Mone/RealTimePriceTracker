package com.multibank.realtimepricetracker.core.common

object StockMetadata {
    fun description(symbol: String): String {
        return when (symbol) {
            "AAPL" -> "Consumer electronics and software"
            "GOOG" -> "Search, ads, and cloud services"
            "MSFT" -> "Software, cloud, and AI platforms"
            "NVDA" -> "AI computing and graphics"
            "AMZN" -> "Commerce and cloud infrastructure"
            "TSLA" -> "Electric vehicles and energy"
            "META" -> "Social platforms and immersive tech"
            "NFLX" -> "Streaming entertainment"
            "AMD" -> "High-performance computing chips"
            "INTC" -> "Semiconductors and processors"
            "UBER" -> "Mobility and delivery platform"
            "ORCL" -> "Enterprise software and cloud"
            "IBM" -> "Enterprise technology and consulting"
            "CRM" -> "Customer relationship software"
            "ADBE" -> "Creative and digital media tools"
            "PYPL" -> "Digital payments"
            "SHOP" -> "Commerce platform"
            "QCOM" -> "Wireless and mobile chipsets"
            "AVGO" -> "Infrastructure software and semiconductors"
            "SNOW" -> "Cloud data platform"
            "PLTR" -> "Data analytics platforms"
            "BABA" -> "E-commerce and cloud services"
            "SONY" -> "Electronics, gaming, and entertainment"
            "SAP" -> "Enterprise resource planning software"
            "ZM" -> "Video communications"
            else -> "Tracked in the live price feed"
        }
    }
}
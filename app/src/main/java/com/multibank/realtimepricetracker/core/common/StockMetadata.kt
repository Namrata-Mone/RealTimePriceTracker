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
            "LYFT" -> "Ride-sharing and urban transportation"
            "ORCL" -> "Enterprise software and cloud"
            "IBM" -> "Enterprise technology and consulting"
            "CRM" -> "Customer relationship software"
            "ADBE" -> "Creative and digital media tools"
            "PYPL" -> "Digital payments"
            "SHOP" -> "Commerce platform for online stores"
            "SQ" -> "Payments and business financial tools"
            "SNAP" -> "Social media and camera technology"
            "BABA" -> "E-commerce and cloud services"
            "SONY" -> "Electronics, gaming, and entertainment"
            "TSM" -> "Semiconductor manufacturing"
            "QCOM" -> "Wireless and mobile chipsets"
            "AVGO" -> "Infrastructure software and semiconductors"
            else -> "Tracked in the real time price feed"
        }
    }
}
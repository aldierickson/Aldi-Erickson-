package com.example.data.model

data class Stock(
    val ticker: String,
    val name: String,
    val sector: String,
    val price: Long,
    val previousClose: Long,
    val change: Long,
    val changePercent: Double,
    val dayHigh: Long,
    val dayLow: Long,
    val volumeLots: Long,
    val peRatio: Double,
    val pbvRatio: Double,
    val dividendYield: Double,
    val orderBookBids: List<OrderBookLevel>,
    val orderBookAsks: List<OrderBookLevel>,
    val candleHistory: List<CandleStickData>,
    val lineHistory: List<Float>
)

data class CandleStickData(
    val label: String,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Long
)

data class OrderBookLevel(
    val price: Long,
    val volumeLots: Long
)

data class LearningModule(
    val id: String,
    val title: String,
    val category: String,
    val level: String,
    val readTimeMinutes: Int,
    val summary: String,
    val sections: List<LessonSection>,
    val quiz: List<QuizQuestion>
)

data class LessonSection(
    val title: String,
    val content: String,
    val keyPoints: List<String> = emptyList(),
    val tip: String? = null
)

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class LeaderboardStudent(
    val rank: Int,
    val name: String,
    val university: String,
    val totalEquity: Long,
    val monthlyRoiPercent: Double,
    val winRatePercent: Double,
    val tradesCount: Int,
    val badge: String,
    val isCurrentUser: Boolean = false
)

data class PortfolioSummary(
    val cashBalance: Long,
    val initialCapital: Long,
    val stocksValue: Long,
    val totalEquity: Long,
    val totalUnrealizedPnL: Long,
    val totalUnrealizedPnLPercent: Double,
    val totalRealizedPnL: Long,
    val totalReturnPercent: Double,
    val holdingsWithStock: List<HoldingDetail>
)

data class HoldingDetail(
    val ticker: String,
    val stockName: String,
    val lots: Int,
    val shares: Int, // lots * 100
    val averagePrice: Double,
    val currentPrice: Long,
    val currentValue: Long,
    val totalInvestment: Long,
    val unrealizedPnL: Long,
    val unrealizedPnLPercent: Double
)

package com.example.data

import com.example.data.model.CandleStickData
import com.example.data.model.OrderBookLevel
import com.example.data.model.Stock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

object StockMarketEngine {

    private val initialStocks = listOf(
        Stock(
            ticker = "BBCA",
            name = "Bank Central Asia Tbk",
            sector = "Keuangan",
            price = 10450L,
            previousClose = 10300L,
            change = 150L,
            changePercent = 1.46,
            dayHigh = 10550L,
            dayLow = 10275L,
            volumeLots = 328450L,
            peRatio = 22.4,
            pbvRatio = 4.8,
            dividendYield = 2.7,
            orderBookBids = listOf(
                OrderBookLevel(10450, 48200),
                OrderBookLevel(10425, 36500),
                OrderBookLevel(10400, 62100),
                OrderBookLevel(10375, 29000),
                OrderBookLevel(10350, 41200)
            ),
            orderBookAsks = listOf(
                OrderBookLevel(10475, 24100),
                OrderBookLevel(10500, 58400),
                OrderBookLevel(10525, 31900),
                OrderBookLevel(10550, 47800),
                OrderBookLevel(10575, 18900)
            ),
            candleHistory = listOf(
                CandleStickData("09:00", 10300f, 10350f, 10275f, 10325f, 42000),
                CandleStickData("10:00", 10325f, 10400f, 10300f, 10375f, 58000),
                CandleStickData("11:00", 10375f, 10450f, 10350f, 10425f, 63000),
                CandleStickData("13:30", 10425f, 10475f, 10400f, 10450f, 39000),
                CandleStickData("14:30", 10450f, 10550f, 10425f, 10525f, 72000),
                CandleStickData("15:30", 10525f, 10550f, 10425f, 10450f, 54450)
            ),
            lineHistory = listOf(10300f, 10325f, 10350f, 10375f, 10425f, 10400f, 10450f, 10525f, 10475f, 10450f)
        ),
        Stock(
            ticker = "BBRI",
            name = "Bank Rakyat Indonesia (Persero) Tbk",
            sector = "Keuangan",
            price = 4820L,
            previousClose = 4870L,
            change = -50L,
            changePercent = -1.03,
            dayHigh = 4900L,
            dayLow = 4800L,
            volumeLots = 512300L,
            peRatio = 11.8,
            pbvRatio = 2.3,
            dividendYield = 6.4,
            orderBookBids = listOf(
                OrderBookLevel(4820, 89300),
                OrderBookLevel(4810, 114200),
                OrderBookLevel(4800, 192000),
                OrderBookLevel(4790, 72400),
                OrderBookLevel(4780, 56300)
            ),
            orderBookAsks = listOf(
                OrderBookLevel(4830, 45200),
                OrderBookLevel(4840, 68100),
                OrderBookLevel(4850, 154000),
                OrderBookLevel(4860, 49200),
                OrderBookLevel(4870, 78500)
            ),
            candleHistory = listOf(
                CandleStickData("09:00", 4880f, 4900f, 4850f, 4860f, 78000),
                CandleStickData("10:00", 4860f, 4870f, 4830f, 4840f, 92000),
                CandleStickData("11:00", 4840f, 4850f, 4810f, 4820f, 85000),
                CandleStickData("13:30", 4820f, 4840f, 4800f, 4810f, 71000),
                CandleStickData("14:30", 4810f, 4830f, 4800f, 4830f, 95000),
                CandleStickData("15:30", 4830f, 4840f, 4810f, 4820f, 91300)
            ),
            lineHistory = listOf(4880f, 4870f, 4860f, 4840f, 4830f, 4810f, 4820f, 4800f, 4830f, 4820f)
        ),
        Stock(
            ticker = "TLKM",
            name = "Telkom Indonesia (Persero) Tbk",
            sector = "Infrastruktur",
            price = 2960L,
            previousClose = 2890L,
            change = 70L,
            changePercent = 2.42,
            dayHigh = 2990L,
            dayLow = 2880L,
            volumeLots = 389400L,
            peRatio = 12.1,
            pbvRatio = 2.1,
            dividendYield = 5.8,
            orderBookBids = listOf(
                OrderBookLevel(2960, 65000),
                OrderBookLevel(2950, 84000),
                OrderBookLevel(2940, 112000),
                OrderBookLevel(2930, 49000),
                OrderBookLevel(2920, 68000)
            ),
            orderBookAsks = listOf(
                OrderBookLevel(2970, 38000),
                OrderBookLevel(2980, 52000),
                OrderBookLevel(2990, 89000),
                OrderBookLevel(3000, 142000),
                OrderBookLevel(3010, 31000)
            ),
            candleHistory = listOf(
                CandleStickData("09:00", 2890f, 2910f, 2880f, 2900f, 54000),
                CandleStickData("10:00", 2900f, 2930f, 2890f, 2920f, 68000),
                CandleStickData("11:00", 2920f, 2950f, 2910f, 2940f, 71000),
                CandleStickData("13:30", 2940f, 2970f, 2930f, 2960f, 59000),
                CandleStickData("14:30", 2960f, 2990f, 2950f, 2980f, 79000),
                CandleStickData("15:30", 2980f, 2990f, 2950f, 2960f, 58400)
            ),
            lineHistory = listOf(2890f, 2900f, 2910f, 2930f, 2920f, 2940f, 2960f, 2980f, 2970f, 2960f)
        ),
        Stock(
            ticker = "ASII",
            name = "Astra International Tbk",
            sector = "Perindustrian",
            price = 5150L,
            previousClose = 5100L,
            change = 50L,
            changePercent = 0.98,
            dayHigh = 5200L,
            dayLow = 5075L,
            volumeLots = 194300L,
            peRatio = 6.9,
            pbvRatio = 1.0,
            dividendYield = 7.9,
            orderBookBids = listOf(
                OrderBookLevel(5150, 31000),
                OrderBookLevel(5125, 42000),
                OrderBookLevel(5100, 68000),
                OrderBookLevel(5075, 23000),
                OrderBookLevel(5050, 35000)
            ),
            orderBookAsks = listOf(
                OrderBookLevel(5175, 29000),
                OrderBookLevel(5200, 71000),
                OrderBookLevel(5225, 18000),
                OrderBookLevel(5250, 44000),
                OrderBookLevel(5275, 12000)
            ),
            candleHistory = listOf(
                CandleStickData("09:00", 5100f, 5125f, 5075f, 5100f, 28000),
                CandleStickData("10:00", 5100f, 5150f, 5075f, 5125f, 35000),
                CandleStickData("11:00", 5125f, 5175f, 5125f, 5150f, 42000),
                CandleStickData("13:30", 5150f, 5175f, 5125f, 5150f, 26000),
                CandleStickData("14:30", 5150f, 5200f, 5150f, 5175f, 38000),
                CandleStickData("15:30", 5175f, 5200f, 5125f, 5150f, 25300)
            ),
            lineHistory = listOf(5100f, 5110f, 5125f, 5100f, 5140f, 5150f, 5160f, 5180f, 5175f, 5150f)
        ),
        Stock(
            ticker = "BMRI",
            name = "Bank Mandiri (Persero) Tbk",
            sector = "Keuangan",
            price = 6850L,
            previousClose = 6725L,
            change = 125L,
            changePercent = 1.86,
            dayHigh = 6925L,
            dayLow = 6700L,
            volumeLots = 285000L,
            peRatio = 10.9,
            pbvRatio = 2.1,
            dividendYield = 5.2,
            orderBookBids = listOf(
                OrderBookLevel(6850, 41000),
                OrderBookLevel(6825, 33000),
                OrderBookLevel(6800, 58000),
                OrderBookLevel(6775, 27000),
                OrderBookLevel(6750, 39000)
            ),
            orderBookAsks = listOf(
                OrderBookLevel(6875, 25000),
                OrderBookLevel(6900, 49000),
                OrderBookLevel(6925, 31000),
                OrderBookLevel(6950, 42000),
                OrderBookLevel(6975, 16000)
            ),
            candleHistory = listOf(
                CandleStickData("09:00", 6725f, 6775f, 6700f, 6750f, 41000),
                CandleStickData("10:00", 6750f, 6800f, 6725f, 6775f, 49000),
                CandleStickData("11:00", 6775f, 6850f, 6775f, 6825f, 55000),
                CandleStickData("13:30", 6825f, 6875f, 6800f, 6850f, 38000),
                CandleStickData("14:30", 6850f, 6925f, 6825f, 6900f, 61000),
                CandleStickData("15:30", 6900f, 6925f, 6825f, 6850f, 41000)
            ),
            lineHistory = listOf(6725f, 6750f, 6760f, 6790f, 6820f, 6840f, 6880f, 6910f, 6870f, 6850f)
        ),
        Stock(
            ticker = "GOTO",
            name = "GoTo Gojek Tokopedia Tbk",
            sector = "Teknologi",
            price = 68L,
            previousClose = 66L,
            change = 2L,
            changePercent = 3.03,
            dayHigh = 71L,
            dayLow = 65L,
            volumeLots = 3450000L,
            peRatio = -8.2,
            pbvRatio = 0.9,
            dividendYield = 0.0,
            orderBookBids = listOf(
                OrderBookLevel(68, 620000),
                OrderBookLevel(67, 850000),
                OrderBookLevel(66, 1200000),
                OrderBookLevel(65, 940000),
                OrderBookLevel(64, 480000)
            ),
            orderBookAsks = listOf(
                OrderBookLevel(69, 580000),
                OrderBookLevel(70, 920000),
                OrderBookLevel(71, 740000),
                OrderBookLevel(72, 610000),
                OrderBookLevel(73, 430000)
            ),
            candleHistory = listOf(
                CandleStickData("09:00", 66f, 67f, 65f, 66f, 480000),
                CandleStickData("10:00", 66f, 68f, 66f, 67f, 620000),
                CandleStickData("11:00", 67f, 69f, 67f, 68f, 710000),
                CandleStickData("13:30", 68f, 70f, 67f, 69f, 540000),
                CandleStickData("14:30", 69f, 71f, 68f, 70f, 650000),
                CandleStickData("15:30", 70f, 71f, 67f, 68f, 450000)
            ),
            lineHistory = listOf(66f, 66f, 67f, 67f, 68f, 68f, 69f, 71f, 70f, 68f)
        ),
        Stock(
            ticker = "ICBP",
            name = "Indofood CBP Sukses Makmur Tbk",
            sector = "Konsumer Primer",
            price = 11950L,
            previousClose = 12050L,
            change = -100L,
            changePercent = -0.83,
            dayHigh = 12100L,
            dayLow = 11900L,
            volumeLots = 82400L,
            peRatio = 14.5,
            pbvRatio = 2.9,
            dividendYield = 3.2,
            orderBookBids = listOf(
                OrderBookLevel(11950, 14500),
                OrderBookLevel(11925, 18200),
                OrderBookLevel(11900, 29400),
                OrderBookLevel(11875, 11000),
                OrderBookLevel(11850, 16800)
            ),
            orderBookAsks = listOf(
                OrderBookLevel(11975, 12800),
                OrderBookLevel(12000, 24500),
                OrderBookLevel(12025, 15100),
                OrderBookLevel(12050, 19800),
                OrderBookLevel(12075, 9400)
            ),
            candleHistory = listOf(
                CandleStickData("09:00", 12050f, 12100f, 12000f, 12025f, 14000),
                CandleStickData("10:00", 12025f, 12050f, 11975f, 12000f, 16000),
                CandleStickData("11:00", 12000f, 12025f, 11950f, 11975f, 13000),
                CandleStickData("13:30", 11975f, 12000f, 11925f, 11950f, 11000),
                CandleStickData("14:30", 11950f, 11975f, 11900f, 11925f, 15400),
                CandleStickData("15:30", 11925f, 11975f, 11925f, 11950f, 13000)
            ),
            lineHistory = listOf(12050f, 12025f, 12000f, 12020f, 11980f, 11960f, 11940f, 11925f, 11940f, 11950f)
        ),
        Stock(
            ticker = "ADRO",
            name = "Adaro Energy Indonesia Tbk",
            sector = "Energi",
            price = 3780L,
            previousClose = 3690L,
            change = 90L,
            changePercent = 2.44,
            dayHigh = 3840L,
            dayLow = 3680L,
            volumeLots = 420000L,
            peRatio = 4.2,
            pbvRatio = 0.9,
            dividendYield = 12.8,
            orderBookBids = listOf(
                OrderBookLevel(3780, 52000),
                OrderBookLevel(3770, 71000),
                OrderBookLevel(3760, 94000),
                OrderBookLevel(3750, 48000),
                OrderBookLevel(3740, 62000)
            ),
            orderBookAsks = listOf(
                OrderBookLevel(3790, 39000),
                OrderBookLevel(3800, 83000),
                OrderBookLevel(3810, 42000),
                OrderBookLevel(3820, 55000),
                OrderBookLevel(3830, 28000)
            ),
            candleHistory = listOf(
                CandleStickData("09:00", 3690f, 3720f, 3680f, 3710f, 65000),
                CandleStickData("10:00", 3710f, 3740f, 3700f, 3730f, 74000),
                CandleStickData("11:00", 3730f, 3770f, 3720f, 3760f, 81000),
                CandleStickData("13:30", 3760f, 3790f, 3750f, 3770f, 62000),
                CandleStickData("14:30", 3770f, 3840f, 3760f, 3810f, 88000),
                CandleStickData("15:30", 3810f, 3830f, 3770f, 3780f, 50000)
            ),
            lineHistory = listOf(3690f, 3700f, 3720f, 3740f, 3760f, 3750f, 3780f, 3820f, 3800f, 3780f)
        )
    )

    private val _stocks = MutableStateFlow(initialStocks)
    val stocks: StateFlow<List<Stock>> = _stocks.asStateFlow()

    fun getStock(ticker: String): Stock? {
        return _stocks.value.find { it.ticker.equals(ticker, ignoreCase = true) }
    }

    // Function to simulate dynamic market ticks
    fun simulateMarketTick() {
        val current = _stocks.value.toMutableList()
        val randomIndex = Random.nextInt(current.size)
        val s = current[randomIndex]

        // Tick fraction rule
        val tickSize = when {
            s.price < 200 -> 1L
            s.price < 500 -> 2L
            s.price < 2000 -> 5L
            s.price < 5000 -> 10L
            else -> 25L
        }

        val step = if (Random.nextBoolean()) tickSize else -tickSize
        val newPrice = (s.price + step).coerceAtLeast(tickSize)
        val newChange = newPrice - s.previousClose
        val newChangePercent = (newChange.toDouble() / s.previousClose) * 100.0
        val newHigh = maxOf(s.dayHigh, newPrice)
        val newLow = minOf(s.dayLow, newPrice)

        val updatedLine = s.lineHistory.takeLast(14) + listOf(newPrice.toFloat())

        current[randomIndex] = s.copy(
            price = newPrice,
            change = newChange,
            changePercent = String.format("%.2f", newChangePercent).toDoubleOrNull() ?: newChangePercent,
            dayHigh = newHigh,
            dayLow = newLow,
            lineHistory = updatedLine
        )

        _stocks.value = current
    }
}

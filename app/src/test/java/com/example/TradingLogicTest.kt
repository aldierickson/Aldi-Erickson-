package com.example

import com.example.data.CurriculumData
import com.example.data.StockMarketEngine
import com.example.ui.components.CurrencyUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TradingLogicTest {

    @Test
    fun testCurrencyFormatting() {
        val formatted = CurrencyUtils.formatRupiah(100_000_000L)
        assertTrue(formatted.contains("100.000.000"))
        assertTrue(formatted.startsWith("Rp"))
    }

    @Test
    fun testPercentFormatting() {
        val positive = CurrencyUtils.formatPercent(3.45)
        assertEquals("+3.45%", positive)

        val negative = CurrencyUtils.formatPercent(-2.10)
        assertEquals("-2.10%", negative)
    }

    @Test
    fun testStockMarketEngineContainsStocks() {
        val bbca = StockMarketEngine.getStock("BBCA")
        assertNotNull(bbca)
        assertEquals("BBCA", bbca?.ticker)
        assertTrue(bbca!!.orderBookBids.isNotEmpty())
        assertTrue(bbca.orderBookAsks.isNotEmpty())
        assertTrue(bbca.candleHistory.isNotEmpty())
    }

    @Test
    fun testCurriculumModulesAvailable() {
        val modules = CurriculumData.modules
        assertTrue(modules.size >= 4)
        modules.forEach { module ->
            assertTrue(module.sections.isNotEmpty())
            assertTrue(module.quiz.isNotEmpty())
            module.quiz.forEach { q ->
                assertTrue(q.correctIndex in 0 until q.options.size)
            }
        }
    }
}

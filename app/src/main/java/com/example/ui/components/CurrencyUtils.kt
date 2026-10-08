package com.example.ui.components

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object CurrencyUtils {

    private val idrFormat: DecimalFormat = run {
        val symbols = DecimalFormatSymbols(Locale("id", "ID")).apply {
            currencySymbol = "Rp "
            groupingSeparator = '.'
            decimalSeparator = ','
        }
        DecimalFormat("#,##0", symbols)
    }

    fun formatRupiah(amount: Long): String {
        return "Rp " + idrFormat.format(amount)
    }

    fun formatRupiah(amount: Double): String {
        return "Rp " + idrFormat.format(amount)
    }

    fun formatPercent(percent: Double, showPlus: Boolean = true): String {
        val sign = if (showPlus && percent > 0) "+" else ""
        return String.format(Locale.US, "%s%.2f%%", sign, percent)
    }

    fun formatLot(lots: Int): String {
        return "$lots Lot"
    }

    fun formatVolume(lots: Long): String {
        return when {
            lots >= 1_000_000 -> String.format(Locale.US, "%.1fM Lot", lots / 1_000_000.0)
            lots >= 1_000 -> String.format(Locale.US, "%.1fK Lot", lots / 1_000.0)
            else -> "$lots Lot"
        }
    }
}

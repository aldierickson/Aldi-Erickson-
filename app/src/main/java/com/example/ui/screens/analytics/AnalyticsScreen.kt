package com.example.ui.screens.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HoldingDetail
import com.example.data.model.Stock
import com.example.ui.components.CurrencyUtils
import com.example.ui.components.PortfolioAllocationDonut
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AnalyticsScreen(
    viewModel: MainViewModel,
    onSelectStock: (Stock) -> Unit,
    modifier: Modifier = Modifier
) {
    val portfolio by viewModel.portfolioSummary.collectAsState()
    val transactions by viewModel.transactions.collectAsState()
    val stocks by viewModel.stocks.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Ringkasan & Alokasi, 1: Kepemilikan, 2: Riwayat Transaksi
    var showResetDialog by remember { mutableStateOf(false) }

    val sellTxs = transactions.filter { it.type == "SELL" }
    val winTrades = sellTxs.count { it.realizedPnL > 0 }
    val winRate = if (sellTxs.isNotEmpty()) (winTrades.toDouble() / sellTxs.size) * 100.0 else 0.0

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("analytics_screen_list"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Dashboard Analitik Portofolio",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Pelacakan performa simulasi & alokasi aset mahasiswa KSPM",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = { showResetDialog = true },
                    modifier = Modifier.testTag("reset_portfolio_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Portofolio",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Main Performance Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL NILAI EKUITAS",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (portfolio.totalReturnPercent >= 0) BullishGreen.copy(alpha = 0.15f) else BearishRed.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Return: ${CurrencyUtils.formatPercent(portfolio.totalReturnPercent)}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (portfolio.totalReturnPercent >= 0) BullishGreen else BearishRed,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = CurrencyUtils.formatRupiah(portfolio.totalEquity),
                        style = MaterialTheme.typography.displayLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Unrealized P/L",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = (if (portfolio.totalUnrealizedPnL >= 0) "+" else "") +
                                        CurrencyUtils.formatRupiah(portfolio.totalUnrealizedPnL) +
                                        " (${CurrencyUtils.formatPercent(portfolio.totalUnrealizedPnLPercent)})",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (portfolio.totalUnrealizedPnL >= 0) BullishGreen else BearishRed
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Realized P/L",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = (if (portfolio.totalRealizedPnL >= 0) "+" else "") +
                                        CurrencyUtils.formatRupiah(portfolio.totalRealizedPnL),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (portfolio.totalRealizedPnL >= 0) BullishGreen else BearishRed
                            )
                        }
                    }
                }
            }
        }

        // Tab Navigation for Analytics
        item {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Alokasi & Metrik") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Saham (${portfolio.holdingsWithStock.size})") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("Jurnal Transaksi (${transactions.size})") }
                )
            }
        }

        if (selectedTab == 0) {
            // Asset Allocation Donut Chart
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Komposisi Alokasi Aset",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        PortfolioAllocationDonut(
                            cashAmount = portfolio.cashBalance,
                            stocksAmount = portfolio.stocksValue
                        )
                    }
                }
            }

            // Key Trading Performance Metrics Grid
            item {
                Text(
                    text = "Metrik Trading Mahasiswa",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Win Rate",
                        value = String.format(Locale.US, "%.1f%%", winRate),
                        sub = "${winTrades} dari ${sellTxs.size} order jual",
                        color = BullishGreen,
                        modifier = Modifier.weight(1f)
                    )

                    MetricCard(
                        title = "Total Transaksi",
                        value = "${transactions.size}",
                        sub = "Frekuensi eksekusi",
                        color = NavyLight,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Modal Awal",
                        value = CurrencyUtils.formatRupiah(portfolio.initialCapital),
                        sub = "Modal virtual KSPM",
                        color = GoldAccent,
                        modifier = Modifier.weight(1f)
                    )

                    val topStock = portfolio.holdingsWithStock.maxByOrNull { it.unrealizedPnL }
                    MetricCard(
                        title = "Top Performer",
                        value = topStock?.ticker ?: "-",
                        sub = if (topStock != null) CurrencyUtils.formatPercent(topStock.unrealizedPnLPercent) else "Belum ada posisi",
                        color = if (topStock != null && topStock.unrealizedPnLPercent >= 0) BullishGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else if (selectedTab == 1) {
            // Holdings List
            if (portfolio.holdingsWithStock.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "📈", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Belum Ada Kepemilikan Saham",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Gunakan saldo kas RDN Anda di menu Simulasi Trading untuk membeli saham emiten IDX.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(portfolio.holdingsWithStock) { holding ->
                    HoldingItemCard(
                        holding = holding,
                        onClick = {
                            stocks.find { it.ticker == holding.ticker }?.let { onSelectStock(it) }
                        }
                    )
                }
            }
        } else {
            // Transactions History Log
            if (transactions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "📝", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Belum Ada Riwayat Transaksi",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Semua transaksi beli dan jual saham akan tercatat rapi di jurnal trading ini.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(transactions) { tx ->
                    val isBuy = tx.type == "BUY"
                    val timeFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                    val dateString = timeFormat.format(Date(tx.timestamp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isBuy) BullishGreen.copy(alpha = 0.15f) else BearishRed.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = tx.type,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isBuy) BullishGreen else BearishRed,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Text(
                                        text = "${tx.ticker} • ${tx.lots} Lot",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = dateString,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = CurrencyUtils.formatRupiah(tx.totalValue),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                if (!isBuy) {
                                    Text(
                                        text = "P/L: " + (if (tx.realizedPnL >= 0) "+" else "") + CurrencyUtils.formatRupiah(tx.realizedPnL),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (tx.realizedPnL >= 0) BullishGreen else BearishRed
                                    )
                                } else {
                                    Text(
                                        text = "@ Rp ${tx.pricePerShare}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Reset Portfolio Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(text = "Reset Portofolio Simulasi?", fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Semua kepemilikan saham dan riwayat transaksi akan dihapus, dan saldo kas RDN Anda akan dikembalikan ke modal awal Rp 100.000.000.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetPortfolio()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BearishRed)
                ) {
                    Text("Ya, Reset Portofolio")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
fun HoldingItemCard(
    holding: HoldingDetail,
    onClick: () -> Unit
) {
    val isPositive = holding.unrealizedPnL >= 0
    val trendColor = if (isPositive) BullishGreen else BearishRed

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = holding.ticker,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${holding.lots} Lot (${holding.shares} Lembar)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = CurrencyUtils.formatRupiah(holding.currentValue),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = trendColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = (if (isPositive) "+" else "") +
                                    CurrencyUtils.formatRupiah(holding.unrealizedPnL) +
                                    " (${CurrencyUtils.formatPercent(holding.unrealizedPnLPercent)})",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = trendColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Harga Beli Rata-rata: Rp ${holding.averagePrice.toInt()}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Harga Saat Ini: Rp ${holding.currentPrice}",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    sub: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = sub,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.sp
            )
        }
    }
}

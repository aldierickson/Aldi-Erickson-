package com.example.ui.screens.trading

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.Stock
import com.example.ui.components.CandlestickChart
import androidx.activity.compose.BackHandler
import com.example.ui.components.CurrencyUtils
import com.example.ui.components.OrderBookView
import com.example.ui.components.SparklineChart
import com.example.ui.components.StockItemCard
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TradingScreen(
    viewModel: MainViewModel,
    onBack: (() -> Unit)? = null
) {
    val stocks by viewModel.stocks.collectAsState()
    val portfolio by viewModel.portfolioSummary.collectAsState()
    val selectedStock by viewModel.selectedStock.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedSector by remember { mutableStateOf("Semua") }
    var orderTypeDialog by remember { mutableStateOf<String?>(null) } // "BUY" or "SELL"

    val sectors = listOf("Semua", "Keuangan", "Infrastruktur", "Konsumer Primer", "Energi", "Teknologi", "Perindustrian")

    val filteredStocks = stocks.filter { stock ->
        val matchesQuery = stock.ticker.contains(searchQuery, ignoreCase = true) ||
                stock.name.contains(searchQuery, ignoreCase = true)
        val matchesSector = selectedSector == "Semua" || stock.sector.equals(selectedSector, ignoreCase = true)
        matchesQuery && matchesSector
    }

    if (selectedStock != null) {
        BackHandler {
            viewModel.selectStock(null)
        }

        StockDetailView(
            stock = selectedStock!!,
            cashBalance = portfolio.cashBalance,
            ownedLots = portfolio.holdingsWithStock.find { it.ticker == selectedStock!!.ticker }?.lots ?: 0,
            onBack = { viewModel.selectStock(null) },
            onOpenBuyDialog = { orderTypeDialog = "BUY" },
            onOpenSellDialog = { orderTypeDialog = "SELL" }
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .testTag("trading_screen_list"),
            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                            text = "Simulasi Trading IDX",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Bursa Efek Indonesia • Simulasi Fraksi Resmi",
                            style = MaterialTheme.typography.bodyMedium,
                            color = NavyLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("stock_search_input"),
                    placeholder = { Text("Cari kode saham atau emiten (cth: BBCA)...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Cari")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sector Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(sectors) { sector ->
                        FilterChip(
                            selected = selectedSector == sector,
                            onClick = { selectedSector = sector },
                            label = { Text(sector) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyLight,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            items(filteredStocks) { stock ->
                StockItemCard(
                    stock = stock,
                    onClick = { viewModel.selectStock(stock) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Buy/Sell Order Execution Dialog
    if (orderTypeDialog != null && selectedStock != null) {
        val stock = selectedStock!!
        val isBuy = orderTypeDialog == "BUY"
        val ownedLots = portfolio.holdingsWithStock.find { it.ticker == stock.ticker }?.lots ?: 0

        OrderExecutionDialog(
            stock = stock,
            isBuy = isBuy,
            cashBalance = portfolio.cashBalance,
            ownedLots = ownedLots,
            onDismiss = { orderTypeDialog = null },
            onConfirmOrder = { lots ->
                if (isBuy) {
                    viewModel.buyStock(stock.ticker, lots)
                } else {
                    viewModel.sellStock(stock.ticker, lots)
                }
                orderTypeDialog = null
            }
        )
    }
}

@Composable
fun StockDetailView(
    stock: Stock,
    cashBalance: Long,
    ownedLots: Int,
    onBack: () -> Unit,
    onOpenBuyDialog: () -> Unit,
    onOpenSellDialog: () -> Unit
) {
    var chartMode by remember { mutableIntStateOf(0) } // 0: Candlestick, 1: Line Chart
    val isPositive = stock.change >= 0
    val trendColor = if (isPositive) BullishGreen else BearishRed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("stock_detail_view")
    ) {
        // Top App Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("back_from_stock_detail")
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Kembali")
            }
            Spacer(modifier = Modifier.width(4.dp))
            Column {
                Text(
                    text = "${stock.ticker} • ${stock.sector}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stock.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Price Banner
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = CurrencyUtils.formatRupiah(stock.price),
                            style = MaterialTheme.typography.displayLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${if (isPositive) "+" else ""}${stock.change} (${CurrencyUtils.formatPercent(stock.changePercent)})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = trendColor
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Hari ini",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (ownedLots > 0) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = NavyLight.copy(alpha = 0.15f)
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "Kepemilikan",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = NavyLight
                                )
                                Text(
                                    text = "$ownedLots Lot",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyLight
                                )
                            }
                        }
                    }
                }
            }

            // Chart Type Selector
            item {
                TabRow(selectedTabIndex = chartMode) {
                    Tab(
                        selected = chartMode == 0,
                        onClick = { chartMode = 0 },
                        text = { Text("Candlestick (OHLC)") }
                    )
                    Tab(
                        selected = chartMode == 1,
                        onClick = { chartMode = 1 },
                        text = { Text("Line Trend") }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    if (chartMode == 0) {
                        CandlestickChart(
                            candles = stock.candleHistory,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                        )
                    } else {
                        SparklineChart(
                            dataPoints = stock.lineHistory,
                            isPositive = isPositive,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    }
                }
            }

            // Key Statistics Grid
            item {
                Text(
                    text = "Statistik & Rasio Kunci",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            StatItem("Tertinggi (High)", CurrencyUtils.formatRupiah(stock.dayHigh))
                            StatItem("Terendah (Low)", CurrencyUtils.formatRupiah(stock.dayLow))
                            StatItem("Volume", CurrencyUtils.formatVolume(stock.volumeLots))
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            StatItem("P/E Ratio", "${stock.peRatio}x")
                            StatItem("PBV Ratio", "${stock.pbvRatio}x")
                            StatItem("Div. Yield", "${stock.dividendYield}%")
                        }
                    }
                }
            }

            // Order Book View
            item {
                Text(
                    text = "Order Book Antrian BEI (Bid & Offer)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                OrderBookView(
                    bids = stock.orderBookBids,
                    asks = stock.orderBookAsks
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Bottom Trading Action Bar
        Surface(
            tonalElevation = 6.dp,
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onOpenBuyDialog,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("button_open_buy"),
                    colors = ButtonDefaults.buttonColors(containerColor = BullishGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "BELI (BUY)",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                Button(
                    onClick = onOpenSellDialog,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("button_open_sell"),
                    colors = ButtonDefaults.buttonColors(containerColor = BearishRed),
                    shape = RoundedCornerShape(12.dp),
                    enabled = ownedLots > 0
                ) {
                    Text(
                        text = if (ownedLots > 0) "JUAL (SELL)" else "BELUM ADA LOT",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun OrderExecutionDialog(
    stock: Stock,
    isBuy: Boolean,
    cashBalance: Long,
    ownedLots: Int,
    onDismiss: () -> Unit,
    onConfirmOrder: (lots: Int) -> Unit
) {
    var lotCount by remember { mutableIntStateOf(1) }

    val shares = lotCount * 100
    val gross = shares * stock.price
    val feeRate = if (isBuy) 0.0015 else 0.0025 // 0.15% beli, 0.25% jual
    val fee = (gross * feeRate).toLong()
    val totalAmount = if (isBuy) gross + fee else gross - fee

    val maxLotsPossible = if (isBuy) {
        val costPerLotWithFee = (100 * stock.price * 1.0015).toLong()
        if (costPerLotWithFee > 0) (cashBalance / costPerLotWithFee).toInt().coerceAtLeast(0) else 0
    } else {
        ownedLots
    }

    val isValid = if (isBuy) {
        lotCount > 0 && totalAmount <= cashBalance
    } else {
        lotCount in 1..ownedLots
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isBuy) "Order Beli (BUY) ${stock.ticker}" else "Order Jual (SELL) ${stock.ticker}",
                fontWeight = FontWeight.Bold,
                color = if (isBuy) BullishGreen else BearishRed
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isBuy) "Saldo Kas RDN:" else "Lot Dimiliki:",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = if (isBuy) CurrencyUtils.formatRupiah(cashBalance) else "$ownedLots Lot",
                        fontWeight = FontWeight.Bold,
                        color = if (isBuy) BullishGreen else NavyLight
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Harga Saham:", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = CurrencyUtils.formatRupiah(stock.price),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Lots Stepper
                Text(
                    text = "Jumlah Lot (1 Lot = 100 Lembar):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { if (lotCount > 1) lotCount-- },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Kurang")
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$lotCount Lot",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "= $shares Lembar",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { if (lotCount < maxLotsPossible) lotCount++ },
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah")
                    }
                }

                // Quick lot preset buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(1, 5, 10, 50).forEach { preset ->
                        OutlinedButton(
                            onClick = { lotCount = preset.coerceAtMost(maxOf(maxLotsPossible, 1)) },
                            modifier = Modifier.weight(1f),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp, horizontal = 2.dp)
                        ) {
                            Text("+$preset", fontSize = 11.sp)
                        }
                    }
                    OutlinedButton(
                        onClick = { lotCount = maxOf(maxLotsPossible, 1) },
                        modifier = Modifier.weight(1.2f),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 4.dp, horizontal = 2.dp)
                    ) {
                        Text("Maks", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Breakdown Summary Card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Nilai Transaksi", style = MaterialTheme.typography.bodySmall)
                            Text(text = CurrencyUtils.formatRupiah(gross), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Fee BEI & Broker (${if (isBuy) "0.15%" else "0.25%"})", style = MaterialTheme.typography.bodySmall)
                            Text(text = CurrencyUtils.formatRupiah(fee), style = MaterialTheme.typography.bodySmall)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = if (isBuy) "Total Bayar" else "Penerimaan Bersih", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            Text(
                                text = CurrencyUtils.formatRupiah(totalAmount),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isBuy) BullishGreen else BearishRed
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmOrder(lotCount) },
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isBuy) BullishGreen else BearishRed
                ),
                modifier = Modifier.testTag("confirm_order_button")
            ) {
                Text(
                    text = if (isBuy) "Konfirmasi Beli" else "Konfirmasi Jual",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

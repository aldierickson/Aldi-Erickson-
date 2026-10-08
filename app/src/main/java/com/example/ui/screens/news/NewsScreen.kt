package com.example.ui.screens.news

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.gemini.MarketNewsSnippet
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NewsScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val marketState by viewModel.marketUpdateState.collectAsState()
    val context = LocalContext.current

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var selectedNewsDetail by remember { mutableStateOf<MarketNewsSnippet?>(null) }

    val categories = listOf("Semua", "IHSG & Makro", "Emiten & Saham", "Regulasi OJK", "Edukasi & Analisis")

    val timeFormat = remember { SimpleDateFormat("HH:mm 'WIB'", Locale("id", "ID")) }
    val formattedTime = remember(marketState.lastUpdated) { timeFormat.format(Date(marketState.lastUpdated)) }

    // Curated rich news library for Indonesian capital markets
    val extendedNews: List<MarketNewsSnippet> = remember(marketState.newsSnippets) {
        val base = marketState.newsSnippets.toMutableList()
        if (base.isEmpty()) {
            base.addAll(
                listOf(
                    MarketNewsSnippet(
                        title = "IHSG Tembus Level Tertinggi Baru Didorong Sentimen Positif Aliran Modal Asing",
                        summary = "Indeks Harga Saham Gabungan (IHSG) menguat signifikan menyusul rilis data makro ekonomi Indonesia yang solid dan apresiasi nilai tukar Rupiah terhadap Dolar AS.",
                        sourceName = "CNBC Indonesia",
                        sourceUrl = "https://www.cnbcindonesia.com/market",
                        category = "IHSG & Makro"
                    ),
                    MarketNewsSnippet(
                        title = "Sektor Perbankan Big-4 Catatkan Pertumbuhan Laba Bersih Q3 Double Digit",
                        summary = "BBCA, BBRI, BMRI, dan BBNI melaporkan pertumbuhan kredit produktif yang sehat didukung marjin bunga bersih (NIM) terjaga dan rasio NPL yang terkendali.",
                        sourceName = "Kontan",
                        sourceUrl = "https://investasi.kontan.co.id",
                        category = "Emiten & Saham"
                    ),
                    MarketNewsSnippet(
                        title = "OJK dan BEI Terbitkan Regulasi Baru Perlindungan Investor Retail dan Transparansi Short Selling",
                        summary = "Otoritas Jasa Keuangan memperketat aturan keterbukaan informasi emiten dan mekanisme pembiayaan transaksi margin untuk meningkatkan perlindungan investor ritel mahasiswa.",
                        sourceName = "Bisnis Indonesia",
                        sourceUrl = "https://market.bisnis.com",
                        category = "Regulasi OJK"
                    ),
                    MarketNewsSnippet(
                        title = "Tips Analisis Laporan Keuangan: Memahami Price to Book Value (PBV) dan ROE bagi Investor Pemula",
                        summary = "Panduan edukasi komparatif fundamental emiten IDX untuk menentukan valuasi wajar saham sebelum mengambil keputusan investasi jangka panjang.",
                        sourceName = "IDX Channel",
                        sourceUrl = "https://www.idxchannel.com",
                        category = "Edukasi & Analisis"
                    ),
                    MarketNewsSnippet(
                        title = "Sektor Energi dan Hilirisasi Mineral Mendapatkan Momentum Positif Permintaan Global",
                        summary = "Saham pertambangan dan energi terbarukan kembali menarik minat fund manager domestik seiring stabilnya harga komoditas nikel dan tembaga.",
                        sourceName = "Investor Daily",
                        sourceUrl = "https://investor.id",
                        category = "Emiten & Saham"
                    )
                )
            )
        }
        base
    }

    val filteredNews = extendedNews.filter { news ->
        val matchesCategory = when (selectedCategory) {
            "Semua" -> true
            "IHSG & Makro" -> news.category.contains("IHSG", ignoreCase = true) || news.category.contains("Makro", ignoreCase = true) || news.title.contains("IHSG", ignoreCase = true) || news.title.contains("Makro", ignoreCase = true)
            "Emiten & Saham" -> news.category.contains("Emiten", ignoreCase = true) || news.category.contains("Saham", ignoreCase = true) || news.title.contains("Saham", ignoreCase = true) || news.title.contains("Bank", ignoreCase = true)
            "Regulasi OJK" -> news.category.contains("Regulasi", ignoreCase = true) || news.category.contains("OJK", ignoreCase = true) || news.title.contains("OJK", ignoreCase = true) || news.title.contains("BEI", ignoreCase = true)
            "Edukasi & Analisis" -> news.category.contains("Edukasi", ignoreCase = true) || news.category.contains("Analisis", ignoreCase = true) || news.title.contains("Analisis", ignoreCase = true) || news.title.contains("Tips", ignoreCase = true)
            else -> true
        }
        val matchesQuery = searchQuery.isBlank() ||
                news.title.contains(searchQuery, ignoreCase = true) ||
                news.summary.contains(searchQuery, ignoreCase = true) ||
                (news.sourceName?.contains(searchQuery, ignoreCase = true) == true)

        matchesCategory && matchesQuery
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("news_screen_root")
    ) {
        // Header
        Surface(
            color = NavyDark,
            tonalElevation = 4.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(NavyDark, NavyPrimary)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Kembali",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Berita & Kabar Pasar",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = BullishGreen.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        text = "LIVE",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = BullishGreen,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Pembaruan Terkini: $formattedTime",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.refreshMarketUpdates() },
                        enabled = !marketState.isLoading
                    ) {
                        if (marketState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Muat Ulang Berita",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Live Market Overview Ticker Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = BullishGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Ringkasan Pasar Saham IDX",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = BullishGreen.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "Sentimen Bullish",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BullishGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "IHSG Composite", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "7.728,40 (+0,48%)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = BullishGreen)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "Volume Transaksi", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "21,4 Miliar Lembar", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "Net Foreign Buy", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = "+Rp 842 M", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = BullishGreen)
                            }
                        }
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_news_input"),
                    placeholder = { Text("Cari berita, emiten, atau kata kunci...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Hapus")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            // Category Chips
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // News List Items
            if (filteredNews.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Newspaper,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tidak ada berita ditemukan untuk pencarian ini.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredNews) { newsItem ->
                    NewsItemCard(
                        news = newsItem,
                        onClick = { selectedNewsDetail = newsItem }
                    )
                }
            }
        }
    }

    // Modal dialog to read full news details
    selectedNewsDetail?.let { news ->
        AlertDialog(
            onDismissRequest = { selectedNewsDetail = null },
            title = {
                Text(
                    text = news.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = NavyLight.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = news.sourceName ?: "Bursa Efek Indonesia",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = NavyLight,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = news.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = news.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Ringkasan untuk Mahasiswa:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = NavyPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Berita ini relevan bagi analisis fundamental dan valuasi sektor pasar modal Indonesia. Perhatikan dampaknya terhadap pergerakan harga saham terkait.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val targetUrl = news.sourceUrl ?: "https://www.idx.co.id"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl))
                        context.startActivity(intent)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Buka Sumber Asli")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedNewsDetail = null }) {
                    Text("Tutup")
                }
            }
        )
    }
}

@Composable
fun NewsItemCard(
    news: MarketNewsSnippet,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("news_item_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = NavyLight.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = news.sourceName ?: "BEI",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = NavyLight,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = news.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = news.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = news.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Baca Analisis & Detail →",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = NavyPrimary
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

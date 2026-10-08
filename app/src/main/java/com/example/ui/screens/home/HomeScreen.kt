package com.example.ui.screens.home

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Stock
import com.example.ui.components.CurrencyUtils
import com.example.ui.components.MarketNewsSection
import com.example.ui.components.StockItemCard
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.NavigationTab

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateTab: (NavigationTab) -> Unit,
    onSelectStock: (Stock) -> Unit
) {
    val stocks by viewModel.stocks.collectAsState()
    val forumPosts by viewModel.forumPosts.collectAsState()
    val leaderboard by viewModel.leaderboard.collectAsState()
    val marketState by viewModel.marketUpdateState.collectAsState()

    val topMovers = stocks.sortedByDescending { it.changePercent }.take(5)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_scroll"),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Hero Header: KSPM Welcome & University Badge
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(NavyDark, NavyPrimary)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = GoldAccent.copy(alpha = 0.2f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.School,
                                            contentDescription = null,
                                            tint = GoldAccent,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "KSPM STAIMI Hub",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldAccent
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White.copy(alpha = 0.15f),
                                    modifier = Modifier.clickable { onNavigateTab(NavigationTab.WEB) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Language,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Buka Website (HTML)",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Halo, Calon Investor!",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Kelompok Studi Pasar Modal • KSPM STAIMI",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }

                        // Official KSPM Bull Logo
                        Image(
                            painter = painterResource(id = R.drawable.ic_kspm_logo),
                            contentDescription = "Logo Resmi KSPM",
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Live Market News Featured Card (Replaces Portfolio View)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateTab(NavigationTab.NEWS) }
                            .testTag("featured_market_news_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.12f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = BullishGreen.copy(alpha = 0.35f)
                                    ) {
                                        Text(
                                            text = "BERITA PASAR TERKINI",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = GoldAccent.copy(alpha = 0.25f)
                                    ) {
                                        Text(
                                            text = "IHSG 7.728 (+0.48%)",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = GoldAccent,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Buka Berita",
                                    tint = Color.White.copy(alpha = 0.8f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            val topNews = marketState.newsSnippets.firstOrNull()
                            Text(
                                text = topNews?.title ?: "IHSG Menguat Ditopang Arus Modal Asing Masuk ke Saham Perbankan & Blue Chip",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = topNews?.summary ?: "Investor merespons positif stabilitas makro ekonomi dan rilis kinerja keuangan emiten kuartal terbaru di Bursa Efek Indonesia.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sumber: ${topNews?.sourceName ?: "CNBC Indonesia • Kontan"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.7f)
                                )

                                Text(
                                    text = "Lihat Seluruh Berita →",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldAccent
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Navigation Tiles
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Fitur Utama KSPM",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        QuickNavButton(
                            title = "Berita\nTerkini",
                            icon = Icons.Default.Newspaper,
                            color = NavyLight,
                            modifier = Modifier.width(96.dp)
                        ) { onNavigateTab(NavigationTab.NEWS) }
                    }

                    item {
                        QuickNavButton(
                            title = "Simulasi\nTrading",
                            icon = Icons.AutoMirrored.Filled.TrendingUp,
                            color = BullishGreen,
                            modifier = Modifier.width(96.dp)
                        ) { onNavigateTab(NavigationTab.TRADE) }
                    }

                    item {
                        QuickNavButton(
                            title = "Modul\nBelajar",
                            icon = Icons.Default.AutoStories,
                            color = Color(0xFF0284C7),
                            modifier = Modifier.width(96.dp)
                        ) { onNavigateTab(NavigationTab.LEARN) }
                    }

                    item {
                        QuickNavButton(
                            title = "Forum\nDiskusi",
                            icon = Icons.Default.Forum,
                            color = Color(0xFF8B5CF6),
                            modifier = Modifier.width(96.dp)
                        ) { onNavigateTab(NavigationTab.FORUM) }
                    }

                    item {
                        QuickNavButton(
                            title = "Peringkat\nBulanan",
                            icon = Icons.Default.EmojiEvents,
                            color = GoldAccent,
                            modifier = Modifier.width(96.dp)
                        ) { onNavigateTab(NavigationTab.LEADERBOARD) }
                    }
                }
            }
        }

        // Web Portal Banner (HTML)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onNavigateTab(NavigationTab.WEB) }
                    .testTag("open_web_portal_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = NavyLight.copy(alpha = 0.12f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, NavyLight.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(NavyPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Buka Portal Website KSPM",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = BullishGreen.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "HTML5",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = BullishGreen,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Tampilan antarmuka website lengkap dengan trading, kuis & forum.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Buka Website",
                        tint = NavyLight
                    )
                }
            }
        }

        // Daily Market News Grounded with Google Search
        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                MarketNewsSection(
                    marketState = marketState,
                    onRefresh = { viewModel.refreshMarketUpdates() }
                )
            }
        }

        // Monthly Leaderboard Spotlight Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onNavigateTab(NavigationTab.LEADERBOARD) }
                    .testTag("leaderboard_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GoldAccent.copy(alpha = 0.12f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(GoldAccent.copy(alpha = 0.25f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "🏆",
                                fontSize = 22.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Kompetisi Portofolio Bulan Ini",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            val leader = leaderboard.firstOrNull()
                            Text(
                                text = "Peringkat 1: ${leader?.name ?: "-"} (${CurrencyUtils.formatPercent(leader?.monthlyRoiPercent ?: 0.0)})",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Buka Peringkat",
                        tint = GoldAccent
                    )
                }
            }
        }

        // Top Market Movers
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Saham IDX Teraktif Hari Ini",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Lihat Semua",
                        style = MaterialTheme.typography.labelMedium,
                        color = NavyLight,
                        modifier = Modifier.clickable { onNavigateTab(NavigationTab.TRADE) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        items(topMovers) { stock ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                StockItemCard(
                    stock = stock,
                    onClick = { onSelectStock(stock) }
                )
            }
        }

        // Community Forum Buzz Card
        item {
            val latestPost = forumPosts.firstOrNull()
            if (latestPost != null) {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Diskusi Populer KSPM",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateTab(NavigationTab.FORUM) },
                        shape = RoundedCornerShape(16.dp),
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
                                    color = NavyLight.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = latestPost.tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = NavyLight,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Text(
                                    text = "${latestPost.likesCount} Suka • ${latestPost.commentsCount} Komentar",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = latestPost.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = latestPost.content,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Oleh ${latestPost.authorName} (${latestPost.authorBadge})",
                                style = MaterialTheme.typography.labelSmall,
                                color = NavyLight
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun QuickNavButton(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }
    }
}

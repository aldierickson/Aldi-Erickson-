package com.example.ui.screens.leaderboard

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Stars
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LeaderboardStudent
import com.example.ui.components.CurrencyUtils
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.viewmodel.MainViewModel

@Composable
fun LeaderboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val leaderboard by viewModel.leaderboard.collectAsState()
    val portfolio by viewModel.portfolioSummary.collectAsState()

    val top1 = leaderboard.getOrNull(0)
    val top2 = leaderboard.getOrNull(1)
    val top3 = leaderboard.getOrNull(2)
    val userEntry = leaderboard.find { it.isCurrentUser }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("leaderboard_screen_list"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            // Header Title
            Column {
                Text(
                    text = "Peringkat Portofolio Bulanan",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kompetisi simulasi trading sehat antar-mahasiswa pasar modal KSPM.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Countdown / Rules Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = GoldAccent.copy(alpha = 0.12f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GoldAccent.copy(alpha = 0.25f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Kompetisi Periode Oktober 2026",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Peringkat diperbarui secara live berdasarkan ROI portofolio simulasi. Hadiah dana RDN riil untuk 3 besar!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Top 3 Podium
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "PODIUM JUARA BULAN INI",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Rank 2 (Silver)
                        if (top2 != null) {
                            PodiumColumn(
                                student = top2,
                                rank = 2,
                                medal = "🥈",
                                podiumHeight = 90.dp,
                                color = Color(0xFFC0C0C0)
                            )
                        }

                        // Rank 1 (Gold)
                        if (top1 != null) {
                            PodiumColumn(
                                student = top1,
                                rank = 1,
                                medal = "🥇",
                                podiumHeight = 120.dp,
                                color = GoldAccent
                            )
                        }

                        // Rank 3 (Bronze)
                        if (top3 != null) {
                            PodiumColumn(
                                student = top3,
                                rank = 3,
                                medal = "🥉",
                                podiumHeight = 75.dp,
                                color = Color(0xFFCD7F32)
                            )
                        }
                    }
                }
            }
        }

        // Current User Standing Banner
        if (userEntry != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = NavyPrimary
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "#${userEntry.rank}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = "Posisi Anda Saat Ini",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = userEntry.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${userEntry.university} • ${userEntry.tradesCount} Transaksi",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = CurrencyUtils.formatPercent(userEntry.monthlyRoiPercent),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (userEntry.monthlyRoiPercent >= 0) BullishGreen else BearishRed
                            )
                            Text(
                                text = CurrencyUtils.formatRupiah(userEntry.totalEquity),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        // Leaderboard List Table
        item {
            Text(
                text = "Papan Peringkat Lengkap Mahasiswa",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(leaderboard) { student ->
            LeaderboardItemRow(student = student)
        }

        // Reward Info Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Stars, contentDescription = null, tint = GoldAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Hadiah & Apresiasi Juara KSPM",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "🥇 Juara 1: Saldo RDN Riil Rp 1.500.000 + Sertifikat OJK & KSPM\n" +
                                "🥈 Juara 2: Saldo RDN Riil Rp 1.000.000 + Sertifikat\n" +
                                "🥉 Juara 3: Saldo RDN Riil Rp 500.000 + Sertifikat\n" +
                                "• Syarat: Mahasiswa aktif, minimal menyelesaikan 3 modul belajar dan 5 transaksi simulasi.",
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun PodiumColumn(
    student: LeaderboardStudent,
    rank: Int,
    medal: String,
    podiumHeight: androidx.compose.ui.unit.Dp,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(96.dp)
    ) {
        Text(text = medal, fontSize = 28.sp)

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = student.name.split(" ").take(2).joinToString(" "),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            textAlign = TextAlign.Center
        )

        Text(
            text = CurrencyUtils.formatPercent(student.monthlyRoiPercent),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = BullishGreen,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Podium Block
        Surface(
            modifier = Modifier
                .width(80.dp)
                .height(podiumHeight),
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            color = color.copy(alpha = 0.25f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "#$rank",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            }
        }
    }
}

@Composable
fun LeaderboardItemRow(student: LeaderboardStudent) {
    val isUser = student.isCurrentUser
    val isPositive = student.monthlyRoiPercent >= 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUser) NavyLight.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUser) 2.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1.3f)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            when (student.rank) {
                                1 -> GoldAccent.copy(alpha = 0.2f)
                                2 -> Color(0xFFC0C0C0).copy(alpha = 0.2f)
                                3 -> Color(0xFFCD7F32).copy(alpha = 0.2f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${student.rank}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = when (student.rank) {
                            1 -> GoldAccent
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = student.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = if (isUser) FontWeight.Bold else FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "${student.university} • ${student.tradesCount} Trx",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = CurrencyUtils.formatPercent(student.monthlyRoiPercent),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isPositive) BullishGreen else BearishRed
                )
                Text(
                    text = CurrencyUtils.formatRupiah(student.totalEquity),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

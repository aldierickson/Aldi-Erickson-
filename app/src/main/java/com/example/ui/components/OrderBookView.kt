package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.OrderBookLevel
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen

@Composable
fun OrderBookView(
    bids: List<OrderBookLevel>,
    asks: List<OrderBookLevel>,
    modifier: Modifier = Modifier
) {
    val maxBidVol = bids.maxOfOrNull { it.volumeLots }?.coerceAtLeast(1L) ?: 1L
    val maxAskVol = asks.maxOfOrNull { it.volumeLots }?.coerceAtLeast(1L) ?: 1L
    val maxVol = maxOf(maxBidVol, maxAskVol).toFloat()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(10.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "BID (Beli)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = BullishGreen,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "ANTRIAN LOT",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Text(
                text = "OFFER (Jual)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = BearishRed,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }

        val rowsCount = maxOf(bids.size, asks.size)
        for (i in 0 until rowsCount) {
            val bid = bids.getOrNull(i)
            val ask = asks.getOrNull(i)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Bid Side (Left)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (bid != null) {
                        // Depth bar from right to left
                        val bidRatio = (bid.volumeLots.toFloat() / maxVol).coerceIn(0.05f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(bidRatio)
                                .align(Alignment.CenterEnd)
                                .clip(RoundedCornerShape(4.dp))
                                .background(BullishGreen.copy(alpha = 0.18f))
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = CurrencyUtils.formatVolume(bid.volumeLots),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp
                            )
                            Text(
                                text = "${bid.price}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BullishGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Ask Side (Right)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (ask != null) {
                        val askRatio = (ask.volumeLots.toFloat() / maxVol).coerceIn(0.05f, 1f)
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(askRatio)
                                .align(Alignment.CenterStart)
                                .clip(RoundedCornerShape(4.dp))
                                .background(BearishRed.copy(alpha = 0.18f))
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${ask.price}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = BearishRed
                            )
                            Text(
                                text = CurrencyUtils.formatVolume(ask.volumeLots),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

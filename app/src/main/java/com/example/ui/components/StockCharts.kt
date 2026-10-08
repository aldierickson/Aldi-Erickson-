package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CandleStickData
import com.example.ui.theme.BearishRed
import com.example.ui.theme.BullishGreen
import com.example.ui.theme.NavyLight

@Composable
fun CandlestickChart(
    candles: List<CandleStickData>,
    modifier: Modifier = Modifier,
    bullishColor: Color = BullishGreen,
    bearishColor: Color = BearishRed
) {
    if (candles.isEmpty()) return

    val minPrice = candles.minOf { it.low }
    val maxPrice = candles.maxOf { it.high }
    val priceRange = if (maxPrice - minPrice > 0) maxPrice - minPrice else 1f
    val maxVolume = candles.maxOf { it.volume }.coerceAtLeast(1L).toFloat()

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val chartHeight = canvasHeight * 0.72f // Upper 72% for candles
                val volumeTop = canvasHeight * 0.78f
                val volumeHeight = canvasHeight * 0.22f

                // Draw subtle grid lines
                val gridLines = 4
                for (i in 0..gridLines) {
                    val y = chartHeight * (i.toFloat() / gridLines)
                    drawLine(
                        color = Color.Gray.copy(alpha = 0.15f),
                        start = Offset(0f, y),
                        end = Offset(canvasWidth, y),
                        strokeWidth = 1f
                    )
                }

                val count = candles.size
                val candleSlot = canvasWidth / count
                val candleWidth = candleSlot * 0.58f

                candles.forEachIndexed { index, candle ->
                    val isBullish = candle.close >= candle.open
                    val color = if (isBullish) bullishColor else bearishColor

                    val centerX = candleSlot * index + candleSlot / 2f

                    // Candle price y positions
                    val highY = chartHeight - ((candle.high - minPrice) / priceRange) * chartHeight
                    val lowY = chartHeight - ((candle.low - minPrice) / priceRange) * chartHeight
                    val openY = chartHeight - ((candle.open - minPrice) / priceRange) * chartHeight
                    val closeY = chartHeight - ((candle.close - minPrice) / priceRange) * chartHeight

                    val bodyTop = minOf(openY, closeY)
                    val bodyHeight = maxOf(Math.abs(closeY - openY), 3f)

                    // Draw wick (shadow)
                    drawLine(
                        color = color,
                        start = Offset(centerX, highY),
                        end = Offset(centerX, lowY),
                        strokeWidth = 2.5f,
                        cap = StrokeCap.Round
                    )

                    // Draw body
                    drawRect(
                        color = color,
                        topLeft = Offset(centerX - candleWidth / 2f, bodyTop),
                        size = Size(candleWidth, bodyHeight)
                    )

                    // Draw volume bar at the bottom
                    val volBarHeight = (candle.volume.toFloat() / maxVolume) * volumeHeight
                    val volY = canvasHeight - volBarHeight
                    drawRect(
                        color = color.copy(alpha = 0.35f),
                        topLeft = Offset(centerX - candleWidth / 2f, volY),
                        size = Size(candleWidth, volBarHeight)
                    )
                }
            }
        }

        // X-axis time labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            candles.forEach { candle ->
                Text(
                    text = candle.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 9.sp
                )
            }
        }
    }
}

@Composable
fun SparklineChart(
    dataPoints: List<Float>,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    if (dataPoints.size < 2) return

    val lineColor = if (isPositive) BullishGreen else BearishRed

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val min = dataPoints.minOrNull() ?: 0f
        val max = dataPoints.maxOrNull() ?: 1f
        val range = if (max - min > 0) max - min else 1f

        val stepX = width / (dataPoints.size - 1)

        val path = Path()
        val fillPath = Path()

        dataPoints.forEachIndexed { index, value ->
            val x = index * stepX
            val y = height - ((value - min) / range) * (height - 6f) - 3f

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
        }

        fillPath.lineTo(width, height)
        fillPath.close()

        // Draw soft gradient area under line
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.25f), lineColor.copy(alpha = 0.0f))
            )
        )

        // Draw line
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.5f, cap = StrokeCap.Round)
        )
    }
}

@Composable
fun PortfolioAllocationDonut(
    cashAmount: Long,
    stocksAmount: Long,
    modifier: Modifier = Modifier
) {
    val total = (cashAmount + stocksAmount).coerceAtLeast(1L).toFloat()
    val cashPercent = (cashAmount.toFloat() / total) * 100f
    val stocksPercent = (stocksAmount.toFloat() / total) * 100f

    val cashSweep = (cashAmount.toFloat() / total) * 360f
    val stocksSweep = 360f - cashSweep

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Box(
            modifier = Modifier.size(110.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val stroke = 22f
                val radius = (size.minDimension - stroke) / 2
                val topLeft = Offset((size.width - radius * 2) / 2, (size.height - radius * 2) / 2)
                val arcSize = Size(radius * 2, radius * 2)

                // Cash arc (Navy)
                drawArc(
                    color = NavyLight,
                    startAngle = -90f,
                    sweepAngle = cashSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )

                // Stocks arc (Bullish Green)
                drawArc(
                    color = BullishGreen,
                    startAngle = -90f + cashSweep,
                    sweepAngle = stocksSweep,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Alokasi",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Aset",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(NavyLight, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Kas RDN (${String.format("%.1f", cashPercent)}%)",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = CurrencyUtils.formatRupiah(cashAmount),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(BullishGreen, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "Saham Portofolio (${String.format("%.1f", stocksPercent)}%)",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = CurrencyUtils.formatRupiah(stocksAmount),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

package com.multibank.realtimepricetracker.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multibank.realtimepricetracker.core.ui.theme.PriceDownColor
import com.multibank.realtimepricetracker.core.ui.theme.PriceUpColor

@Composable
fun PriceChangeIndicator(
    currentPrice: Double,
    previousPrice: Double?,
) {
    val isUp = previousPrice != null && currentPrice > previousPrice
    val isDown = previousPrice != null && currentPrice < previousPrice

    val arrow = when {
        isUp -> "↑"
        isDown -> "↓"
        else -> "•"
    }

    val color: Color = when {
        isUp -> PriceUpColor
        isDown -> PriceDownColor
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(30.dp)
    ) {
        Text(
            text = arrow,
            fontSize = 25.sp,
            color = color,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
package com.multibank.realtimepricetracker.presentation.feed.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.multibank.realtimepricetracker.core.ui.components.PriceChangeIndicator
import com.multibank.realtimepricetracker.core.ui.theme.PriceDownColor
import com.multibank.realtimepricetracker.core.ui.theme.PriceUpColor
import com.multibank.realtimepricetracker.domain.model.StockPrice
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun StockPriceRow(
    stock: StockPrice,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var flashColor by remember(stock.symbol) { mutableStateOf(Color.Transparent) }

    LaunchedEffect(stock.currentPrice, stock.previousPrice) {
        if (stock.previousPrice != null && stock.currentPrice != stock.previousPrice) {
            flashColor = if (stock.currentPrice > stock.previousPrice) {
                PriceUpColor.copy(alpha = 0.16f)
            } else {
                PriceDownColor.copy(alpha = 0.16f)
            }

            delay(1000)
            flashColor = Color.Transparent
        }
    }

    val animatedFlashColor by animateColorAsState(
        targetValue = flashColor,
        animationSpec = tween(durationMillis = 220),
        label = "row_flash"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(stock.symbol) },
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stock.symbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    PriceChangeIndicator(
                        currentPrice = stock.currentPrice,
                        previousPrice = stock.previousPrice
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = String.format(Locale.US, "$%.2f", stock.currentPrice),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(animatedFlashColor, RoundedCornerShape(20.dp))
        )
    }
}
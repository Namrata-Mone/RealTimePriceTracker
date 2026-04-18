package com.multibank.realtimepricetracker.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.multibank.realtimepricetracker.core.ui.theme.PriceDownColor
import com.multibank.realtimepricetracker.core.ui.theme.PriceUpColor
import com.multibank.realtimepricetracker.domain.model.ConnectionStatus

@Composable
fun StatusIndicator(
    status: ConnectionStatus,
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    val dotColor = when (status) {
        ConnectionStatus.CONNECTED -> PriceUpColor
        ConnectionStatus.CONNECTING -> Color(0xFFFFB300)
        ConnectionStatus.DISCONNECTED -> PriceDownColor
        ConnectionStatus.ERROR -> MaterialTheme.colorScheme.error
    }

    val label = when (status) {
        ConnectionStatus.CONNECTED -> "Connected"
        ConnectionStatus.CONNECTING -> "Connecting"
        ConnectionStatus.DISCONNECTED -> "Disconnected"
        ConnectionStatus.ERROR -> "Error"
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(dotColor, CircleShape)
        )

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = textColor
        )
    }
}
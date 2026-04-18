package com.multibank.realtimepricetracker.presentation.feed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.multibank.realtimepricetracker.presentation.feed.viewmodel.FeedViewModel

@Composable
fun FeedTestScreen(
    viewModel: FeedViewModel = hiltViewModel()
) {
    val prices by viewModel.prices.collectAsStateWithLifecycle()
    val connectionStatus by viewModel.connectionStatus.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Connection: $connectionStatus",
            style = MaterialTheme.typography.titleMedium
        )

        Button(
            onClick = viewModel::startConnection,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Start connection")
        }

        OutlinedButton(
            onClick = viewModel::stopConnection,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Stop connection")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(prices, key = { it.symbol }) { stock ->
                Text(
                    text = "${stock.symbol} - ${stock.currentPrice}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
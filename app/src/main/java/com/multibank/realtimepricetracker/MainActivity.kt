package com.multibank.realtimepricetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.multibank.realtimepricetracker.core.ui.theme.RealTimePriceTrackerTheme
import com.multibank.realtimepricetracker.presentation.navigation.RealTimePriceTrackerNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealTimePriceTrackerTheme {
                RealTimePriceTrackerNavHost()
            }
        }
    }
}
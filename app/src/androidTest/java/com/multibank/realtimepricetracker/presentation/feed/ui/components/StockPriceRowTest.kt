package com.multibank.realtimepricetracker.presentation.feed.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.multibank.realtimepricetracker.domain.model.StockPrice
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class StockPriceRowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun stockPriceRow_displays_symbol_and_price() {
        val stock = StockPrice(
            symbol = "AAPL",
            currentPrice = 182.45,
            previousPrice = 180.10,
            timestamp = System.currentTimeMillis()
        )

        composeTestRule.setContent {
            StockPriceRow(
                stock = stock,
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText("AAPL").assertIsDisplayed()
        composeTestRule.onNodeWithText("$182.45").assertIsDisplayed()
    }

    @Test
    fun stockPriceRow_invokes_click_callback() {
        val stock = StockPrice(
            symbol = "AAPL",
            currentPrice = 182.45,
            previousPrice = 180.10,
            timestamp = System.currentTimeMillis()
        )

        var clickedSymbol = ""

        composeTestRule.setContent {
            StockPriceRow(
                stock = stock,
                onClick = { clickedSymbol = it }
            )
        }

        composeTestRule.onNodeWithText("AAPL").performClick()

        assertEquals("AAPL", clickedSymbol)
    }
}
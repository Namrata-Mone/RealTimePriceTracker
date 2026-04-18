package com.multibank.realtimepricetracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.multibank.realtimepricetracker.presentation.details.ui.SymbolDetailsScreen
import com.multibank.realtimepricetracker.presentation.feed.ui.FeedScreen

@Composable
fun RealTimePriceTrackerNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.FEED
    ) {
        composable(NavRoutes.FEED) {
            FeedScreen(
                onSymbolClick = { symbol ->
                    navController.navigate(NavRoutes.details(symbol))
                }
            )
        }

        composable(
            route = NavRoutes.DETAILS,
            arguments = listOf(
                navArgument("symbol") {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "stocks://symbol/{symbol}"
                }
            )
        ) {
            SymbolDetailsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
package com.multibank.realtimepricetracker.core.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val PrimaryLight = Color(0xFF1565C0)
val SecondaryLight = Color(0xFF546E7A)
val BackgroundLight = Color(0xFFF7F9FC)
val SurfaceLight = Color(0xFFFFFFFF)
val OnPrimaryLight = Color(0xFFFFFFFF)
val OnSecondaryLight = Color(0xFFFFFFFF)
val OnBackgroundLight = Color(0xFF1A1C1E)
val OnSurfaceLight = Color(0xFF1A1C1E)

val PrimaryDark = Color(0xFF90CAF9)
val SecondaryDark = Color(0xFFB0BEC5)
val BackgroundDark = Color(0xFF101418)
val SurfaceDark = Color(0xFF171C22)
val OnPrimaryDark = Color(0xFF003258)
val OnSecondaryDark = Color(0xFF22323B)
val OnBackgroundDark = Color(0xFFE2E2E6)
val OnSurfaceDark = Color(0xFFE2E2E6)

val PriceUpColor = Color(0xFF2EAF61)
val PriceDownColor = Color(0xFFD64545)

val LightColorScheme = lightColorScheme(
    primary = PrimaryLight,
    secondary = SecondaryLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = OnPrimaryLight,
    onSecondary = OnSecondaryLight,
    onBackground = OnBackgroundLight,
    onSurface = OnSurfaceLight
)

val DarkColorScheme = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    background = BackgroundDark,
    surface = SurfaceDark,
    onPrimary = OnPrimaryDark,
    onSecondary = OnSecondaryDark,
    onBackground = OnBackgroundDark,
    onSurface = OnSurfaceDark
)
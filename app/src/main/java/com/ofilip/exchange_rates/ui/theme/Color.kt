package com.ofilip.exchange_rates.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Immutable
data class ExtendedColors(
    val primaryGradient: Brush,
    val onPrimaryGradient: Color
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        primaryGradient = Brush.horizontalGradient(
            colors = listOf()
        ),
        onPrimaryGradient = Color.Unspecified
    )
}

val Purple200 = Color(0xFF953FEC)
val Purple500 = Color(0xFF573FED)
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)
val LightGray = Color(0xFFB8B8B8)
val XLightGray = Color(0xFFEEEEEE)
val DarkGray = Color(0xFF1E2021)
val Red = Color(0xFFF5615B)
val Transparent = Color(0x00000000)

package com.ofilip.exchange_rates.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Brush
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Purple500,
    onPrimary = White,
    secondary = Purple200,
    onSecondary = White,
    background = Black,
    onBackground = LightGray,
    surface = DarkGray,
    onSurface = White,
    error = Red,
    onError = White
)

val DarkExtendedColors = ExtendedColors(
    primaryGradient = Brush.horizontalGradient(
        colors = listOf(Purple500, Purple200)
    ),
    onPrimaryGradient = White
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    onPrimary = White,
    secondary = Purple200,
    onSecondary = White,
    background = XLightGray,
    onBackground = DarkGray,
    surface = White,
    onSurface = Black,
    error = Red,
    onError = White
)

val LightExtendedColors = ExtendedColors(
    primaryGradient = Brush.horizontalGradient(
        colors = listOf(Purple500, Purple200)
    ),
    onPrimaryGradient = White
)

@Composable
fun ExchangeRatesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()

    if (darkTheme) {
        systemUiController.setSystemBarsColor(
            color = Black,
            darkIcons = false
        )
    } else {
        systemUiController.setSystemBarsColor(
            color = XLightGray,
            darkIcons = true
        )
    }

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    CompositionLocalProvider(
        LocalExtendedColors provides
                if (darkTheme) DarkExtendedColors else LightExtendedColors,
    ) {
        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

object ExtendedTheme {
    val color: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}

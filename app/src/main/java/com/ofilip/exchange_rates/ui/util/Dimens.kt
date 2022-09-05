package com.ofilip.exchange_rates.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.ofilip.exchange_rates.R

object Dimens {
    @Composable
    @ReadOnlyComposable
    fun screenHorizontalPadding(): Dp = dimensionResource(id = R.dimen.screen_horizontal_padding)

    @Composable
    @ReadOnlyComposable
    fun cardHorizontalPadding(): Dp = dimensionResource(id = R.dimen.card_horizontal_padding)

    @Composable
    @ReadOnlyComposable
    fun cardVerticalPadding(): Dp = dimensionResource(id = R.dimen.card_vertical_padding)

    @Composable
    @ReadOnlyComposable
    fun spacingSmall(): Dp = dimensionResource(id = R.dimen.spacing_small)

    @Composable
    @ReadOnlyComposable
    fun spacingMedium(): Dp = dimensionResource(id = R.dimen.spacing_medium)

    @Composable
    @ReadOnlyComposable
    fun spacingLarge(): Dp = dimensionResource(id = R.dimen.spacing_large)
}

package com.ofilip.exchange_rates.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import kotlinx.serialization.Serializable

// Destinations

@Serializable
object Splash

@Serializable
object Home

@Serializable
data class CurrencySelection(
    val preselectedCurrency: String?
)

@Serializable
data class CurrencyDetail(
    val currencyCode: String
)

@Serializable
object RatesTimeSeries


// Navigation

fun NavController.navigateToHome(
    navOptions: NavOptions? = null,
) {
    navigate(Home, navOptions)
}

fun NavController.navigateToCurrencySelection(
    preselectedCurrency: String?,
    resultCallback: (String?) -> Unit,
    navOptions: NavOptions? = null,
) {
    navigateForResult(CurrencySelection(preselectedCurrency), resultCallback, navOptions)
}

fun NavController.navigateToCurrencyDetail(
    currencyCode: String,
    navOptions: NavOptions? = null,
) {
    navigate(CurrencyDetail(currencyCode), navOptions)
}

fun NavController.navigateToRatesTimeSeries(
    navOptions: NavOptions? = null,
) {
    navigate(RatesTimeSeries, navOptions)
}

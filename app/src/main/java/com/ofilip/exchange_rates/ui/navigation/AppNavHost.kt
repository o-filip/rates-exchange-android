package com.ofilip.exchange_rates.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ofilip.exchange_rates.ui.screen.currencyDetail.CurrencyDetailScreen
import com.ofilip.exchange_rates.ui.screen.currencyDetail.CurrencyDetailScreenDest
import com.ofilip.exchange_rates.ui.screen.currencySelection.CurrencySelectionScreen
import com.ofilip.exchange_rates.ui.screen.currencySelection.CurrencySelectionScreenDest
import com.ofilip.exchange_rates.ui.screen.home.HomeScreen
import com.ofilip.exchange_rates.ui.screen.home.HomeScreenDest
import com.ofilip.exchange_rates.ui.screen.ratesTimeSeries.RatesTimeSeriesScreen
import com.ofilip.exchange_rates.ui.screen.ratesTimeSeries.RatesTimeSeriesScreenDest
import com.ofilip.exchange_rates.ui.screen.splash.SplashScreen
import com.ofilip.exchange_rates.ui.screen.splash.SplashScreenDest

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = SplashScreenDest.route,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable(SplashScreenDest) {
            SplashScreen(
                onNavigateToApp = {
                    navController.navigate(
                        HomeScreenDest.path(),
                        navOptions = NavOptions.Builder()
                            .setPopUpTo(SplashScreenDest.route, inclusive = true)
                            .build()
                    )
                }
            )
        }

        composable(HomeScreenDest) {
            HomeScreen(
                onNavigateToCurrencySelection = { preselectedCurrency, resultCallback ->
                    navController.navigateForResult(
                        CurrencySelectionScreenDest.path(
                            preselectedCurrency?.let { listOf(it) } ?: emptyList()
                        ),
                        navResultCallback = resultCallback
                    )
                },
                onNavigateToCurrencyDetail = { currencyCode ->
                    navController.navigate(CurrencyDetailScreenDest.path(currencyCode))
                },
                onNavigateToRatesTimeSeries = {
                    navController.navigate(
                        RatesTimeSeriesScreenDest.path()
                    )
                }
            )
        }

        composable(CurrencySelectionScreenDest) {
            CurrencySelectionScreen(
                onNavigateBack = { selectedCurrency ->
                    navController.popBackStackWithResult(selectedCurrency)
                }
            )
        }

        composable(CurrencyDetailScreenDest) {
            CurrencyDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(RatesTimeSeriesScreenDest) {
            RatesTimeSeriesScreen(
                onNavigateToCurrencySelection = { preselectedCurrencies, resultCallback ->
                    navController.navigateForResult(
                        CurrencySelectionScreenDest.path(preselectedCurrencies ?: emptyList()),
                        navResultCallback = resultCallback
                    )
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

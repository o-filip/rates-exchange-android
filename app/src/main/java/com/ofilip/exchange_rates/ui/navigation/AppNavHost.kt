package com.ofilip.exchange_rates.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ofilip.exchange_rates.ui.screen.currencyDetail.CurrencyDetailScreen
import com.ofilip.exchange_rates.ui.screen.currencySelection.CurrencySelectionScreen
import com.ofilip.exchange_rates.ui.screen.home.HomeScreen
import com.ofilip.exchange_rates.ui.screen.ratesTimeSeries.RatesTimeSeriesScreen
import com.ofilip.exchange_rates.ui.screen.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Any = Splash,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Splash> {
            SplashScreen(
                onNavigateToApp = {
                    navController.navigateToHome(
                        navOptions = NavOptions.Builder().setPopUpTo(
                            Splash,
                            inclusive = true
                        ).build()
                    )
                }
            )
        }

        composable<Home> {
            HomeScreen(
                onNavigateToCurrencySelection = navController::navigateToCurrencySelection,
                onNavigateToCurrencyDetail = navController::navigateToCurrencyDetail,
                onNavigateToRatesTimeSeries = navController::navigateToRatesTimeSeries
            )
        }

        composable<CurrencySelection> {
            CurrencySelectionScreen(
                onNavigateBack = navController::popBackStackWithResult
            )
        }

        composable<CurrencyDetail> {
            CurrencyDetailScreen(
                onNavigateBack = navController::popBackStack
            )
        }

        composable<RatesTimeSeries> {
            RatesTimeSeriesScreen(
                onNavigateBack = navController::popBackStack,
                onNavigateToCurrencySelection = navController::navigateToCurrencySelection
            )
        }
    }
}

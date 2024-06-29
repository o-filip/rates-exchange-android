package com.ofilip.exchange_rates.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ofilip.exchange_rates.ui.component.NoInternetConnectionStatusBar
import com.ofilip.exchange_rates.ui.navigation.AppNavHost
import com.ofilip.exchange_rates.ui.screen.home.component.themeToggle.ThemeViewModel
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val connectionStatus =
                mainViewModel.internetConnectionStatus.collectAsStateWithLifecycle(null).value
            val isDarkTheme = themeViewModel.isDarkTheme.collectAsStateWithLifecycle(null).value

            ExchangeRatesTheme(
                darkTheme = isDarkTheme ?: isSystemInDarkTheme()
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(Modifier.fillMaxSize()) {
                        if (connectionStatus != null) {
                            AnimatedVisibility(
                                visible = !connectionStatus.isConnected
                            ) {
                                NoInternetConnectionStatusBar(connectionStatus = connectionStatus)
                            }
                        }

                        AppNavHost()
                    }
                }
            }
        }
    }
}

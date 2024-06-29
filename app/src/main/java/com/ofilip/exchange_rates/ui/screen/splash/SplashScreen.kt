package com.ofilip.exchange_rates.ui.screen.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.component.button.SpacerVertLarge
import com.ofilip.exchange_rates.ui.extension.screenHorizontalPadding
import com.ofilip.exchange_rates.ui.navigation.DefaultDest
import com.ofilip.exchange_rates.ui.navigation.Dest
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme

object SplashScreenDest : Dest by DefaultDest("splash")

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    onNavigateToApp: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(uiState.initializedSuccessfully) {
        if (uiState.initializedSuccessfully) {
            onNavigateToApp()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.initApp()
    }

    SplashScreenContent(
        modifier = modifier,
        uiState = uiState,
        onRetry = viewModel::initApp
    )
}

@Composable
fun SplashScreenContent(
    modifier: Modifier = Modifier,
    uiState: SplashUiState,
    onRetry: () -> Unit
) {
    Scaffold(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .screenHorizontalPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineSmall
                )
                SpacerVertLarge()
                Button(onClick = onRetry) {
                    Text(text = stringResource(id = R.string.splash_button_retry))
                }
            }
        }
    }

}

@Preview
@Composable
fun SplashScreenPreviewLight() {
    ExchangeRatesTheme {
        SplashScreenContent(
            uiState = SplashUiState(
                isLoading = false,
                errorMessage = "Error message",
                initializedSuccessfully = false
            ),
            onRetry = {}
        )
    }
}

@Preview
@Composable
fun SplashScreenPreviewDark() {
    ExchangeRatesTheme(darkTheme = true) {
        SplashScreenContent(
            uiState = SplashUiState(
                isLoading = false,
                errorMessage = "Error message",
                initializedSuccessfully = false
            ),
            onRetry = {}
        )
    }
}

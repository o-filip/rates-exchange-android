package com.ofilip.exchange_rates.ui.screen.ratesTimeSeries

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.component.SimpleTopBar
import com.ofilip.exchange_rates.ui.component.button.SpacerVertMedium
import com.ofilip.exchange_rates.ui.navigation.DefaultDest
import com.ofilip.exchange_rates.ui.navigation.Dest
import com.ofilip.exchange_rates.ui.screen.ratesTimeSeries.component.RatesTimeSeriesChart
import com.ofilip.exchange_rates.ui.screen.ratesTimeSeries.component.RatesTimeSeriesFilter

object RatesTimeSeriesScreenDest : Dest by DefaultDest(route = "ratesTimeSeries") {
    fun path(): String = "ratesTimeSeries"
}

@Composable
fun RatesTimeSeriesScreen(
    modifier: Modifier = Modifier,
    viewModel: RatesTimeSeriesViewModel = hiltViewModel(),
    onNavigateToCurrencySelection: (
        preselectedCurrencies: List<String>?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    RatesTimeSeriesScreenContent(
        modifier = modifier,
        uiState = uiState,
        onBaseCurrencyChange = viewModel::onBaseCurrencyCodeChanged,
        onDateRangeChanged = viewModel::onDateRangeChanged,
        onCurrencyChange = viewModel::onCurrencyCodeChanged,
        onNavigateToCurrencySelection = onNavigateToCurrencySelection,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun RatesTimeSeriesScreenContent(
    modifier: Modifier = Modifier,
    uiState: RatesTimeSeriesUiState,
    onBaseCurrencyChange: (String) -> Unit,
    onDateRangeChanged: (RatesTimeSeriesDateRange) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onNavigateToCurrencySelection: (
        preselectedCurrencies: List<String>?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
    onNavigateBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        topBar = {
            SimpleTopBar(
                title = { Text(stringResource(id = R.string.rate_time_series_title)) },
                onNavigateBack = onNavigateBack
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(state = scrollState)
        ) {
            RatesTimeSeriesFilter(
                baseCurrency = uiState.baseCurrencyCode,
                dateRange = uiState.dateRange,
                currency = uiState.currencyCode,
                onBaseCurrencyChange = onBaseCurrencyChange,
                onDateRangeChanged = onDateRangeChanged,
                onCurrencyChange = onCurrencyChange,
                onNavigateToCurrencySelection = onNavigateToCurrencySelection
            )

            SpacerVertMedium()

            RatesTimeSeriesChart(
                modifier = Modifier
                    .fillMaxWidth(),
                data = uiState.chartData,
                errorMessage = uiState.ratesTimeSeriesErrorMessage,
                isLoading = uiState.isLoading
            )
        }
    }
}

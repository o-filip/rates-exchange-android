package com.ofilip.exchange_rates.ui.screen.home.component.currencyRates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.ui.component.button.CurrencySelectionButton
import com.ofilip.exchange_rates.ui.component.button.SpacerVertMedium
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.util.Dimens


@Composable
fun CurrencyRatesSection(
    modifier: Modifier = Modifier,
    viewModel: CurrencyRatesViewModel = hiltViewModel(),
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
    onNavigateToCurrencyDetail: (currencyCode: String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    CurrencyRatesSectionContent(
        modifier = modifier,
        uiState = uiState,
        onNavigateToCurrencySelection = onNavigateToCurrencySelection,
        onNavigateToCurrencyDetail = onNavigateToCurrencyDetail,
        refreshData = viewModel::refreshData,
        onBaseCurrencySelected = viewModel::onBaseCurrencySelected
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrencyRatesSectionContent(
    modifier: Modifier = Modifier,
    uiState: CurrencyRatesUiState,
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
    onNavigateToCurrencyDetail: (currencyCode: String) -> Unit,
    refreshData: () -> Unit,
    onBaseCurrencySelected: (String) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.ratesLoading,
        onRefresh = refreshData
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            contentPadding = PaddingValues(
                bottom = Dimens.spacingSmall()
            )
        ) {
            currencyRatesSection(
                onNavigateToCurrencySelection = onNavigateToCurrencySelection,
                onNavigateToCurrencyDetail = onNavigateToCurrencyDetail,
                rates = uiState.rates,
                baseCurrency = uiState.overviewCurrency,
                baseCurrencyErrorMessage = uiState.baseCurrencyErrorMessage,
                ratesLoadErrorMessage = uiState.ratesLoadErrorMessage,
                onBaseCurrencySelected = onBaseCurrencySelected
            )
        }

        PullRefreshIndicator(
            uiState.ratesLoading,
            pullRefreshState,
            Modifier.align(Alignment.TopCenter)
        )
    }
}

fun LazyListScope.currencyRatesSection(
    baseCurrency: String?,
    baseCurrencyErrorMessage: String?,
    ratesLoadErrorMessage: String?,
    rates: List<CurrencyRate>,
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
    onNavigateToCurrencyDetail: (currencyCode: String) -> Unit,
    onBaseCurrencySelected: (String) -> Unit,
) {
    item {
        CurrencyRatesSectionHeader(
            baseCurrency = baseCurrency,
            baseCurrencyErrorMessage = baseCurrencyErrorMessage,
            ratesLoadErrorMessage = ratesLoadErrorMessage,
            onNavigateToCurrencySelection = onNavigateToCurrencySelection,
            onBaseCurrencySelected = onBaseCurrencySelected
        )
    }

    items(
        rates,
        key = { it.currency },
        itemContent = { currencyRate ->
            CurrencyRateListItem(
                currencyRate = currencyRate,
                onClick = { onNavigateToCurrencyDetail(currencyRate.currency) }
            )
        }
    )

    item {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.cardVerticalPadding()),
            shape = MaterialTheme.shapes.medium.copy(
                topEnd = CornerSize(0.dp),
                topStart = CornerSize(0.dp)
            ),
        ) {}
    }
}

@Composable

fun CurrencyRatesSectionHeader(
    modifier: Modifier = Modifier,
    baseCurrency: String?,
    baseCurrencyErrorMessage: String?,
    ratesLoadErrorMessage: String?,
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
    onBaseCurrencySelected: (String) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium.copy(
            bottomEnd = CornerSize(0.dp),
            bottomStart = CornerSize(0.dp)
        ),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = Dimens.cardHorizontalPadding())
                .padding(top = Dimens.cardVerticalPadding())
                .padding(bottom = Dimens.spacingLarge())
        ) {
            // Headline
            Text(
                text = stringResource(id = R.string.currency_rates_header),
                style = MaterialTheme.typography.h3
            )
            SpacerVertMedium()
            // Error message
            if (ratesLoadErrorMessage != null || baseCurrencyErrorMessage != null) {
                Text(
                    text = when {
                        ratesLoadErrorMessage != null -> ratesLoadErrorMessage
                        baseCurrencyErrorMessage != null -> baseCurrencyErrorMessage
                        else -> "$ratesLoadErrorMessage\n$baseCurrencyErrorMessage"
                    },
                    style = MaterialTheme.typography.body1
                )

                SpacerVertMedium()
            }
            // Base currency selection button
            CurrencySelectionButton(
                onClick = {
                    onNavigateToCurrencySelection(
                        baseCurrency,
                    ) { result ->
                        result?.let { onBaseCurrencySelected(it) }
                    }
                },
                currencyCode = baseCurrency,
                prefixText = "${stringResource(id = R.string.currency_rates_base_currency_button_prefix)}: ",
            )
        }
    }
}


@Preview
@Composable
fun CurrencyRatesSectionContentPreviewLight() {
    ExchangeRatesTheme {
        CurrencyRatesSectionContent(
            uiState = CurrencyRatesUiState(
                rates = listOf(
                    CurrencyRate("EUR", rate = 1.0),
                    CurrencyRate("USD", rate = 1.2),
                    CurrencyRate("GBP", rate = 0.9),
                ),
                overviewCurrency = "EUR",
                ratesLoading = false
            ),
            onNavigateToCurrencySelection = { _, _ -> },
            onNavigateToCurrencyDetail = {},
            refreshData = {},
            onBaseCurrencySelected = {}
        )
    }

}

@Preview
@Composable
fun CurrencyRatesSectionContentPreviewDark() {
    ExchangeRatesTheme(darkTheme = true) {
        CurrencyRatesSectionContent(
            uiState = CurrencyRatesUiState(
                rates = listOf(
                    CurrencyRate("EUR", rate = 1.0),
                    CurrencyRate("USD", rate = 1.2),
                    CurrencyRate("GBP", rate = 0.9),
                ),
                overviewCurrency = "EUR",
                ratesLoading = false
            ),
            onNavigateToCurrencySelection = { _, _ -> },
            onNavigateToCurrencyDetail = {},
            refreshData = {},
            onBaseCurrencySelected = {}
        )
    }
}

package com.ofilip.exchange_rates.ui.screen.home.component.currencyRates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.ui.component.button.CurrencySelectionButton
import com.ofilip.exchange_rates.ui.component.button.SpacerVertMedium
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.util.Dimens


@Composable
fun CurrencyRatesSection(
    modifier: Modifier = Modifier,
    viewModel: RatesOverviewViewModel = hiltViewModel(),
    onNavigateToCurrencySelection: () -> Unit,
    onNavigateToCurrencyDetail: (currencyCode: String) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    CurrencyRatesSectionContent(
        modifier = modifier,
        uiState = uiState,
        onNavigateToCurrencySelection = onNavigateToCurrencySelection,
        onNavigateToCurrencyDetail = onNavigateToCurrencyDetail,
        refreshData = viewModel::refreshData
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrencyRatesSectionContent(
    modifier: Modifier = Modifier,
    uiState: RatesOverviewUiState,
    onNavigateToCurrencySelection: () -> Unit,
    onNavigateToCurrencyDetail: (currencyCode: String) -> Unit,
    refreshData: () -> Unit,
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
        LazyColumn {
            currencyRatesSection(
                onNavigateToCurrencySelection = onNavigateToCurrencySelection,
                onNavigateToCurrencyDetail = onNavigateToCurrencyDetail,
                rates = uiState.rates,
                baseCurrency = uiState.overviewCurrency,
                baseCurrencyErrorMessage = uiState.baseCurrencyErrorMessage,
                ratesLoadErrorMessage = uiState.ratesLoadErrorMessage,
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
    onNavigateToCurrencySelection: () -> Unit,
    onNavigateToCurrencyDetail: (currencyCode: String) -> Unit
) {
    item {
        Card(
            modifier = Modifier
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
                Text(
                    text = stringResource(id = R.string.currency_rates_header),
                    style = MaterialTheme.typography.h3
                )

                SpacerVertMedium()

                if (baseCurrencyErrorMessage != null) {
                    Text(
                        text = baseCurrencyErrorMessage,
                        style = MaterialTheme.typography.body1
                    )

                    SpacerVertMedium()
                }

                if (ratesLoadErrorMessage != null) {
                    Text(
                        text = ratesLoadErrorMessage,
                        style = MaterialTheme.typography.body1
                    )

                    SpacerVertMedium()
                }

                CurrencySelectionButton(
                    onClick = onNavigateToCurrencySelection,
                    currencyCode = baseCurrency,
                    prefixText = "${stringResource(id = R.string.currency_rates_base_currency_button_prefix)}: ",
                )
            }
        }
    }

    items(
        rates,
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


@Preview
@Composable
fun CurrencyRatesSectionContentPreviewLight() {
    ExchangeRatesTheme {
        CurrencyRatesSectionContent(
            uiState = RatesOverviewUiState(
                rates = listOf(
                    CurrencyRate("EUR", 1.0),
                    CurrencyRate("USD", 1.2),
                    CurrencyRate("GBP", 0.9),
                ),
                overviewCurrency = "EUR",
                ratesLoading = false
            ),
            onNavigateToCurrencySelection = {},
            onNavigateToCurrencyDetail = {},
            refreshData = {}
        )
    }

}

@Preview
@Composable
fun CurrencyRatesSectionContentPreviewDark() {
    ExchangeRatesTheme(darkTheme = true) {
        CurrencyRatesSectionContent(
            uiState = RatesOverviewUiState(
                rates = listOf(
                    CurrencyRate("EUR", 1.0),
                    CurrencyRate("USD", 1.2),
                    CurrencyRate("GBP", 0.9),
                ),
                overviewCurrency = "EUR",
                ratesLoading = false
            ),
            onNavigateToCurrencySelection = {},
            onNavigateToCurrencyDetail = {},
            refreshData = {}
        )
    }
}
package com.ofilip.exchange_rates.ui.screen.currencyDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.ui.component.button.ArrowNavBack
import com.ofilip.exchange_rates.ui.component.button.SpacerVertMedium
import com.ofilip.exchange_rates.ui.extension.screenHorizontalPadding
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.util.Dimens


@Composable
fun CurrencyDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrencyDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    CurrencyDetailScreenContent(
        modifier = modifier,
        uiState = uiState,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun CurrencyDetailScreenContent(
    modifier: Modifier = Modifier,
    uiState: CurrencyDetailUiState,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                elevation = 0.dp,
                title = {},
                navigationIcon = {
                    ArrowNavBack(onClick = onNavigateBack)
                },
                backgroundColor = MaterialTheme.colors.background
            )
        }
    ) { contentPadding ->
        when {
            uiState.isLoading -> {
                Loading(modifier = Modifier.padding(contentPadding))
            }

            uiState.errorMessage != null -> {
                ErrorMessage(
                    modifier = Modifier
                        .padding(contentPadding),
                    message = uiState.errorMessage
                )
            }

            uiState.currency != null -> {
                CurrencyDetailBody(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxWidth()
                        .screenHorizontalPadding()
                        .padding(vertical = Dimens.spacingLarge()),
                    currency = uiState.currency
                )
            }
        }
    }
}

@Composable
private fun Loading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorMessage(
    modifier: Modifier = Modifier,
    message: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(
            text = message,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun CurrencyDetailBody(
    modifier: Modifier = Modifier,
    currency: Currency
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier
            .verticalScroll(scrollState)
            .padding(bottom = Dimens.spacingLarge()),
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = Dimens.cardHorizontalPadding(),
                vertical = Dimens.cardVerticalPadding()
            )
        ) {

            if (currency.currencyName != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = currency.currencyName,
                    style = MaterialTheme.typography.h2,
                    textAlign = TextAlign.Center
                )

                Divider(modifier = Modifier.padding(vertical = Dimens.spacingLarge()))
            }

            CurrencyPropertiesList(
                currency = currency
            )
        }
    }
}

@Composable
private fun CurrencyPropertiesList(
    modifier: Modifier = Modifier,
    currency: Currency
) {
    val currencyProperties = remember(currency) {
        mapOf(
            R.string.currency_detail_currency_code_label to currency.currencyCode,
            R.string.currency_detail_symbol_label to currency.symbol,
            R.string.currency_detail_number_code_label to currency.numberCode,
            R.string.currency_detail_precision_label to currency.precision?.toString(),
            R.string.currency_detail_decimal_separator_label to currency.decimalSeparator,
            R.string.currency_detail_thousands_separator_label to currency.thousandsSeparator
        )
    }

    Column(modifier = modifier) {
        currencyProperties.forEach { (labelRes, value) ->
            CurrencyProperty(
                label = stringResource(id = labelRes),
                value = value
            )
        }

        CurrencyProperty(
            label = stringResource(id = R.string.currency_detail_symbol_first_label),
            value = currency.symbolFirst?.let {
                stringResource(
                    id = if (it) R.string.currency_detail_symbol_position_before_value
                    else R.string.currency_detail_symbol_position_after_value
                )
            }
        )
    }
}

@Composable
private fun CurrencyProperty(
    label: String,
    value: String?
) {
    if (value != null) {
        Column {
            Text(text = "$label: $value")

            SpacerVertMedium()
        }
    }
}

@Preview
@Composable
private fun CurrencyDetailScreenContentPreviewLight() {
    ExchangeRatesTheme {
        CurrencyDetailScreenContent(
            uiState = CurrencyDetailUiState(
                currency = Currency(
                    currencyCode = "USD",
                    currencyName = "US Dollar",
                    symbol = "$",
                    precision = 2,
                    symbolFirst = true,
                    decimalSeparator = ".",
                    thousandsSeparator = ",",
                    isFavorite = false,
                    numberCode = "840"
                )
            ),
            onNavigateBack = {}
        )
    }
}

@Preview
@Composable
private fun CurrencyDetailScreenContentPreviewDark() {
    ExchangeRatesTheme(darkTheme = true) {
        CurrencyDetailScreenContent(
            uiState = CurrencyDetailUiState(
                currency = Currency(
                    currencyCode = "USD",
                    currencyName = "US Dollar",
                    symbol = "$",
                    precision = 2,
                    symbolFirst = true,
                    decimalSeparator = ".",
                    thousandsSeparator = ",",
                    isFavorite = false,
                    numberCode = "840"
                )
            ),
            onNavigateBack = {}
        )
    }
}



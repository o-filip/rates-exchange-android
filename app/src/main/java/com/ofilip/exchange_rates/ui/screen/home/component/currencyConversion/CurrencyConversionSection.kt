package com.ofilip.exchange_rates.ui.screen.home.component.currencyConversion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.component.button.SpacerVertMedium
import com.ofilip.exchange_rates.ui.component.field.CurrencyWithAmountField
import com.ofilip.exchange_rates.ui.component.field.DecimalTextFieldValue
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.util.Dimens

@Composable
fun CurrencyConversionSection(
    modifier: Modifier = Modifier,
    viewModel: CurrencyConversionViewModel = hiltViewModel(),
    onSelectConversionCurrencyFrom: () -> Unit,
    onSelectConversionCurrencyTo: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    CurrencyConversionSectionContent(
        uiState = uiState,
        onSelectConversionCurrencyFrom = onSelectConversionCurrencyFrom,
        onSelectConversionCurrencyTo = onSelectConversionCurrencyTo,
        onConvertAmountFromChanged = viewModel::onConvertAmountFromChanged,
        onConvertAmountToChanged = viewModel::onConvertAmountToChanged,
    )
}

@Composable
fun CurrencyConversionSectionContent(
    modifier: Modifier = Modifier,
    uiState: CurrencyConversionUiState,
    onSelectConversionCurrencyFrom: () -> Unit,
    onSelectConversionCurrencyTo: () -> Unit,
    onConvertAmountFromChanged: (DecimalTextFieldValue) -> Unit,
    onConvertAmountToChanged: (DecimalTextFieldValue) -> Unit
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = Dimens.cardHorizontalPadding(),
                    vertical = Dimens.cardVerticalPadding()
                )
        ) {
            Text(
                text = stringResource(id = R.string.currency_conversion_card_title),
                style = MaterialTheme.typography.h3
            )

            SpacerVertMedium()

            CurrencyWithAmountField(
                amount = uiState.convertAmountFrom,
                currencyCode = uiState.convertCurrencyFrom,
                onAmountChanged = onConvertAmountFromChanged,
                onCurrencySelectionClick = onSelectConversionCurrencyFrom,
                label = {
                    Text(text = stringResource(id = R.string.currency_conversion_from_input_label))
                },
                error = uiState.convertCurrencyFromErrorMessage?.let {
                    { Text(text = it) }
                }
            )

            SpacerVertMedium()

            CurrencyWithAmountField(
                amount = uiState.convertAmountTo,
                currencyCode = uiState.convertCurrencyTo,
                onAmountChanged = onConvertAmountToChanged,
                onCurrencySelectionClick = onSelectConversionCurrencyTo,
                label = {
                    Text(text = stringResource(id = R.string.currency_conversion_to_input_label))
                },
                error = uiState.convertCurrencyToErrorMessage?.let {
                    { Text(text = it) }
                }
            )

            if (uiState.conversionErrorMessage != null) {
                SpacerVertMedium()

                Text(
                    text = uiState.conversionErrorMessage,
                    style = MaterialTheme.typography.body1.copy(
                        color = MaterialTheme.colors.error
                    )
                )
            }
        }
    }
}

@Preview
@Composable
fun CurrencyConversionSectionContentPreviewLight() {
    ExchangeRatesTheme {
        CurrencyConversionSectionContent(
            uiState = CurrencyConversionUiState(
                convertAmountFrom = DecimalTextFieldValue(TextFieldValue("1.0"), 1.0),
                convertCurrencyFrom = "EUR",
                convertAmountTo = DecimalTextFieldValue(TextFieldValue("1.2"), 1.2),
                convertCurrencyTo = "USD",
                convertCurrencyFromErrorMessage = null,
                convertCurrencyToErrorMessage = null,
                conversionErrorMessage = null
            ),
            onSelectConversionCurrencyFrom = {},
            onSelectConversionCurrencyTo = {},
            onConvertAmountFromChanged = {},
            onConvertAmountToChanged = {}
        )
    }
}

@Preview
@Composable
fun CurrencyConversionSectionContentPreviewDark() {
    ExchangeRatesTheme(darkTheme = true) {
        CurrencyConversionSectionContent(
            uiState = CurrencyConversionUiState(
                convertAmountFrom = DecimalTextFieldValue(TextFieldValue("1.0"), 1.0),
                convertCurrencyFrom = "EUR",
                convertAmountTo = DecimalTextFieldValue(TextFieldValue("1.2"), 1.2),
                convertCurrencyTo = "USD",
                convertCurrencyFromErrorMessage = null,
                convertCurrencyToErrorMessage = null,
                conversionErrorMessage = null
            ),
            onSelectConversionCurrencyFrom = {},
            onSelectConversionCurrencyTo = {},
            onConvertAmountFromChanged = {},
            onConvertAmountToChanged = {}
        )
    }
}
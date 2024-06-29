package com.ofilip.exchange_rates.ui.screen.home.component.currencyConversion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    CurrencyConversionSectionContent(
        modifier = modifier,
        uiState = uiState,
        onNavigateToCurrencySelection = onNavigateToCurrencySelection,
        onConvertAmountFromChanged = viewModel::onConvertAmountFromChanged,
        onConvertAmountToChanged = viewModel::onConvertAmountToChanged,
        onConvertCurrencyFromSelected = viewModel::setConversionCurrencyFrom,
        onConvertCurrencyToSelected = viewModel::setConversionCurrencyTo
    )
}

@Composable
fun CurrencyConversionSectionContent(
    modifier: Modifier = Modifier,
    uiState: CurrencyConversionUiState,
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
    onConvertAmountFromChanged: (DecimalTextFieldValue) -> Unit,
    onConvertAmountToChanged: (DecimalTextFieldValue) -> Unit,
    onConvertCurrencyFromSelected: (String) -> Unit,
    onConvertCurrencyToSelected: (String) -> Unit
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

            ConversionFromFields(
                uiState = uiState,
                onConvertAmountFromChanged = onConvertAmountFromChanged,
                onNavigateToCurrencySelection = onNavigateToCurrencySelection,
                onConvertCurrencyFromSelected = onConvertCurrencyFromSelected,
                onConvertAmountToChanged = onConvertAmountToChanged,
                onConvertCurrencyToSelected = onConvertCurrencyToSelected
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

@Composable
private fun ConversionFromFields(
    uiState: CurrencyConversionUiState,
    onConvertAmountFromChanged: (DecimalTextFieldValue) -> Unit,
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
    onConvertCurrencyFromSelected: (String) -> Unit,
    onConvertAmountToChanged: (DecimalTextFieldValue) -> Unit,
    onConvertCurrencyToSelected: (String) -> Unit
) {
    Column {
        CurrencyWithAmountField(
            amount = uiState.convertAmountFrom,
            currencyCode = uiState.convertCurrencyFrom,
            onAmountChanged = onConvertAmountFromChanged,
            onCurrencySelectionClick = {
                onNavigateToCurrencySelection(
                    uiState.convertCurrencyFrom
                ) { result ->
                    result?.let { onConvertCurrencyFromSelected(it) }
                }
            },
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
            onCurrencySelectionClick = {
                onNavigateToCurrencySelection(
                    uiState.convertCurrencyTo
                ) { result ->
                    result?.let { onConvertCurrencyToSelected(it) }
                }
            },
            label = {
                Text(text = stringResource(id = R.string.currency_conversion_to_input_label))
            },
            error = uiState.convertCurrencyToErrorMessage?.let {
                { Text(text = it) }
            }
        )
    }
}

@Preview
@Composable
private fun CurrencyConversionSectionContentPreviewLight() {
    ExchangeRatesTheme {
        CurrencyConversionSectionContent(
            uiState = CurrencyConversionUiState(
                convertAmountFrom = DecimalTextFieldValue(TextFieldValue("1.0"), number = 1.0),
                convertCurrencyFrom = "EUR",
                convertAmountTo = DecimalTextFieldValue(TextFieldValue("1.2"), number = 1.2),
                convertCurrencyTo = "USD",
                convertCurrencyFromErrorMessage = null,
                convertCurrencyToErrorMessage = null,
                conversionErrorMessage = null
            ),
            onConvertAmountFromChanged = {},
            onNavigateToCurrencySelection = { _, _ -> },
            onConvertCurrencyToSelected = {},
            onConvertCurrencyFromSelected = {},
            onConvertAmountToChanged = {}
        )
    }
}

@Preview
@Composable
private fun CurrencyConversionSectionContentPreviewDark() {
    ExchangeRatesTheme(darkTheme = true) {
        CurrencyConversionSectionContent(
            uiState = CurrencyConversionUiState(
                convertAmountFrom = DecimalTextFieldValue(TextFieldValue("1.0"), number = 1.0),
                convertCurrencyFrom = "EUR",
                convertAmountTo = DecimalTextFieldValue(TextFieldValue("1.2"), number = 1.2),
                convertCurrencyTo = "USD",
                convertCurrencyFromErrorMessage = null,
                convertCurrencyToErrorMessage = null,
                conversionErrorMessage = null
            ),
            onConvertAmountFromChanged = {},
            onNavigateToCurrencySelection = { _, _ -> },
            onConvertCurrencyToSelected = {},
            onConvertCurrencyFromSelected = {},
            onConvertAmountToChanged = {}
        )
    }
}

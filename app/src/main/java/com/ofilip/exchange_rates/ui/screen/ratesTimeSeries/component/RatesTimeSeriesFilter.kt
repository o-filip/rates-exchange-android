package com.ofilip.exchange_rates.ui.screen.ratesTimeSeries.component

import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.component.button.CurrencySelectionButton
import org.joda.time.DateTime

@Composable
fun RatesTimeSeriesFilter(
    modifier: Modifier = Modifier,
    baseCurrency: String,
    dateFrom: DateTime,
    dateTo: DateTime,
    currencies: List<String>,
    onBaseCurrencyChange: (String) -> Unit,
    onDateFromChange: (DateTime) -> Unit,
    onDateToChange: (DateTime) -> Unit,
    onCurrenciesChange: (List<String>) -> Unit,
    onNavigateToCurrencySelection: (
        preselectedCurrencies: List<String>?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
) {
    Card(
        modifier = modifier
    ) {
        CurrencySelectionButton(
            onClick = {
                onNavigateToCurrencySelection(
                    listOf(baseCurrency)
                ) { result ->
                    result?.let { onBaseCurrencyChange(it) }
                }
            },
            currencyCode = baseCurrency,
            prefixText = "${stringResource(id = R.string.currency_rates_base_currency_button_prefix)}: ",
        )

    }
}
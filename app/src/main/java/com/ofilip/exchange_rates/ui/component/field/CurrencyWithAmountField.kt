package com.ofilip.exchange_rates.ui.component.field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ofilip.exchange_rates.ui.component.button.CurrencySelectionButton
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme

@Composable
fun CurrencyWithAmountField(
    modifier: Modifier = Modifier,
    amount: DecimalTextFieldValue,
    currencyCode: String?,
    onAmountChanged: (DecimalTextFieldValue) -> Unit,
    onCurrencySelectionClick: () -> Unit,
    label: @Composable (() -> Unit)? = null,
    error: @Composable (() -> Unit)? = null
) {
    DecimalNumberField(
        modifier = modifier.fillMaxWidth(),
        value = amount,
        onValueChanged = onAmountChanged,
        leading = {
            CurrencySelectionButton(
                modifier = Modifier.padding(end = 16.dp),
                currencyCode = currencyCode,
                onClick = onCurrencySelectionClick
            )
        },
        label = label,
        error = error
    )
}

@Preview(showBackground = true)
@Composable
fun CurrencyWithAmountFieldPreview() {
    ExchangeRatesTheme {
        CurrencyWithAmountField(
            amount = DecimalTextFieldValue.create(99.5),
            currencyCode = "USD",
            onAmountChanged = {},
            onCurrencySelectionClick = {},
            label = { Text(text = "From") }
        )
    }
}

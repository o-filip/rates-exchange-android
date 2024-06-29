package com.ofilip.exchange_rates.ui.screen.currencySelection.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.component.LabelledCheckBox
import com.ofilip.exchange_rates.ui.component.button.SpacerVertSmall
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.theme.Transparent
import com.ofilip.exchange_rates.ui.util.Dimens

@Composable
fun CurrencyFilterSection(
    modifier: Modifier = Modifier,
    textQuery: TextFieldValue,
    onQueryUpdated: (TextFieldValue) -> Unit,
    showFavoritesOnly: Boolean,
    onToggleShowFavoritesOnly: () -> Unit,
    errorMessage: String?
) {
    Card {
        Column(
            modifier = modifier.padding(Dimens.cardHorizontalPadding())
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = textQuery,
                onValueChange = onQueryUpdated,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Transparent,
                    focusedLabelColor = MaterialTheme.colors.onSurface,
                ),
                label = {
                    Text(text = stringResource(id = R.string.currency_selection_text_query_label))
                }
            )

            SpacerVertSmall()

            LabelledCheckBox(
                checked = showFavoritesOnly,
                onCheckedChange = { onToggleShowFavoritesOnly() },
                label = stringResource(id = R.string.currency_selection_favorites_only)
            )

            if (errorMessage != null) {
                Text(text = errorMessage)
            }
        }
    }
}

@Preview
@Composable
fun CurrencyFilterSectionPreviewLight() {
    ExchangeRatesTheme {
        CurrencyFilterSection(
            textQuery = TextFieldValue(""),
            onQueryUpdated = {},
            showFavoritesOnly = false,
            onToggleShowFavoritesOnly = {},
            errorMessage = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencyFilterSectionPreviewDark() {
    ExchangeRatesTheme(darkTheme = true) {
        CurrencyFilterSection(
            textQuery = TextFieldValue(""),
            onQueryUpdated = {},
            showFavoritesOnly = false,
            onToggleShowFavoritesOnly = {},
            errorMessage = null
        )
    }
}

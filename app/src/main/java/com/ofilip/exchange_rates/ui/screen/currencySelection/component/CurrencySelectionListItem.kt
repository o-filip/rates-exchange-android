package com.ofilip.exchange_rates.ui.screen.currencySelection.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.ui.extension.conditional
import com.ofilip.exchange_rates.ui.component.button.FavoriteButton
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.theme.ExtendedTheme
import com.ofilip.exchange_rates.ui.util.Dimens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CurrencySelectionListItem(
    modifier: Modifier = Modifier,
    currency: Currency,
    isSelected: Boolean,
    onSelected: () -> Unit,
    onFavoriteToggle: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onSelected
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .conditional(isSelected) {
                    background(ExtendedTheme.color.primaryGradient)
                }
                .padding(
                    horizontal = Dimens.cardHorizontalPadding(),
                    vertical = Dimens.spacingSmall()
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                val textStyle = MaterialTheme.typography.body1.let {
                    if (isSelected) it.copy(color = ExtendedTheme.color.onPrimaryGradient) else it
                }

                Text(text = currency.currencyCode, style = textStyle)
                if (currency.currencyName != null) {
                    Text(text = currency.currencyName, style = textStyle)
                }
            }

            FavoriteButton(
                isFavorite = currency.isFavorite,
                onFavoriteToggle = onFavoriteToggle,
                iconTint = if (isSelected) MaterialTheme.colors.onPrimary else MaterialTheme.colors.primary
            )
        }
    }
}

@Preview
@Composable
fun CurrencySelectionListItemPreviewLight() {
    ExchangeRatesTheme {
        CurrencySelectionListItem(
            currency = Currency(
                currencyCode = "EUR",
                currencyName = "Euro",
                isFavorite = false,
                symbolFirst = true,
                symbol = "€",
                decimalSeparator = ",",
                thousandsSeparator = ".",
                precision = 2,
                numberCode = "978",
            ),
            isSelected = false,
            onSelected = {},
            onFavoriteToggle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CurrencySelectionListItemPreviewDark() {
    ExchangeRatesTheme(darkTheme = true) {
        CurrencySelectionListItem(
            currency = Currency(
                currencyCode = "EUR",
                currencyName = "Euro",
                isFavorite = false,
                symbolFirst = true,
                symbol = "€",
                decimalSeparator = ",",
                thousandsSeparator = ".",
                precision = 2,
                numberCode = "978",
            ),
            isSelected = false,
            onSelected = {},
            onFavoriteToggle = {}
        )
    }
}

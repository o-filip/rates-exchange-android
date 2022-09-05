package com.ofilip.exchange_rates.ui.screen.home.component.currencyRates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.util.Dimens
import com.ofilip.exchange_rates.ui.util.formatCurrencyRate

@Composable
fun CurrencyRateListItem(
    modifier: Modifier = Modifier,
    currencyRate: CurrencyRate,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RectangleShape,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = Dimens.cardHorizontalPadding(),
                        vertical = Dimens.spacingSmall()
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = currencyRate.currency)

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = currencyRate.rate?.formatCurrencyRate()
                        ?: stringResource(id = R.string.unknown_rate)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    modifier = Modifier.size(12.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null
                )
            }

            Divider(startIndent = Dimens.cardHorizontalPadding())
        }
    }
}

@Preview
@Composable
fun CurrencyRateListItemPreviewLight() {
    ExchangeRatesTheme {
        CurrencyRateListItem(
            currencyRate = CurrencyRate("EUR", 1.5),
            onClick = {}
        )
    }
}

@Preview
@Composable
fun CurrencyRateListItemPreviewDark() {
    ExchangeRatesTheme(darkTheme = true) {
        CurrencyRateListItem(
            currencyRate = CurrencyRate("EUR", 1.5),
            onClick = {}
        )
    }
}
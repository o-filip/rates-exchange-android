package com.ofilip.exchange_rates.ui.component.button

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.theme.ExtendedTheme

@Composable
fun CurrencySelectionButton(
    modifier: Modifier = Modifier,
    currencyCode: String?,
    prefixText: String? = null,
    onClick: () -> Unit
) {
    GradientButton(
        modifier = modifier,
        onClick = onClick
    ) {

        Text(
            text = "${prefixText ?: ""}${currencyCode ?: ""}",
        )

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            modifier = Modifier.height(14.dp),
            painter = painterResource(id = R.drawable.ic_arrow_down),
            contentDescription = null,
            tint = ExtendedTheme.color.onPrimaryGradient
        )
    }
}

@Preview
@Composable
fun CurrencySelectionButtonPreview() {
    ExchangeRatesTheme {
        CurrencySelectionButton(
            currencyCode = "USD",
            onClick = {}
        )
    }
}


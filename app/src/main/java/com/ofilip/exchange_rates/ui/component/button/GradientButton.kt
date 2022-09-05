package com.ofilip.exchange_rates.ui.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.theme.ExtendedTheme
import com.ofilip.exchange_rates.ui.theme.Transparent

@Composable
fun GradientButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(backgroundColor = Transparent),
        onClick = onClick,
        contentPadding = PaddingValues()
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.button.copy(
                color = ExtendedTheme.color.onPrimaryGradient
            )
        ) {
            Row(
                modifier = Modifier
                    .background(
                        brush = ExtendedTheme.color.primaryGradient,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(ButtonDefaults.ContentPadding),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                content()
            }
        }
    }
}

@Preview
@Composable
fun GradientButtonPreview() {
    ExchangeRatesTheme {
        GradientButton(
            onClick = {}
        ) {
            Text(text = "Button")
        }
    }
}

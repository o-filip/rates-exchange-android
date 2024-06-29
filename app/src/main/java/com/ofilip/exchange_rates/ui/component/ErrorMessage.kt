package com.ofilip.exchange_rates.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ofilip.exchange_rates.ui.util.Dimens

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    message: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.spacingLarge()),
        text = message,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.body1,
        color = MaterialTheme.colors.error,
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorMessagePreview() {
    ErrorMessage(message = "Error message")
}

package com.ofilip.exchange_rates.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.core.entity.InternetConnectionStatus
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.theme.ExtendedTheme
import com.ofilip.exchange_rates.ui.util.Dimens
import com.ofilip.exchange_rates.ui.util.formatDateTime

@Composable
fun NoInternetConnectionStatusBar(
    modifier: Modifier = Modifier,
    connectionStatus: InternetConnectionStatus
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ExtendedTheme.color.primaryGradient)
            .padding(vertical = Dimens.spacingSmall()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = Dimens.spacingMedium()),
            text = stringResource(
                id = R.string.no_internet_connection_status_bar_disconnected,
                connectionStatus.lastDataLoadedTimestampMs?.formatDateTime()
                    ?: ""
            ),
            color = ExtendedTheme.color.onPrimaryGradient
        )

        Text(
            modifier = Modifier
                .padding(horizontal = Dimens.spacingMedium()),
            text = stringResource(
                id = R.string.no_internet_connection_status_bar_used_data_fom,
                connectionStatus.lastDataLoadedTimestampMs?.formatDateTime()
                    ?: ""
            ),
            color = ExtendedTheme.color.onPrimaryGradient,
            style = MaterialTheme.typography.caption
        )
    }
}

@Preview
@Composable
fun NoInternetConnectionStatusBarPreview() {
    ExchangeRatesTheme {
        NoInternetConnectionStatusBar(
            connectionStatus = InternetConnectionStatus(
                isConnected = false,
                lastDataLoadedTimestampMs = 1560798279000
            )
        )
    }
}

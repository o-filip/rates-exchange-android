package com.ofilip.exchange_rates.ui.screen.home


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.component.button.SpacerHorizontalMedium
import com.ofilip.exchange_rates.ui.component.button.SpacerVertMedium
import com.ofilip.exchange_rates.ui.extension.screenHorizontalPadding
import com.ofilip.exchange_rates.ui.screen.home.component.currencyConversion.CurrencyConversionSection
import com.ofilip.exchange_rates.ui.screen.home.component.currencyRates.CurrencyRatesSection
import com.ofilip.exchange_rates.ui.screen.home.component.themeToggle.ThemeToggleSection
import com.ofilip.exchange_rates.ui.util.Dimens
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
    onNavigateToCurrencyDetail: (currencyCode: String) -> Unit,
    onNavigateToRatesTimeSeries: () -> Unit
) {
    CollapsingToolbarScaffold(
        state = rememberCollapsingToolbarScaffoldState(),
        modifier = modifier.clipToBounds(),
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            HomeScreenToolbar(
                onNavigateToRatesTimeSeries = onNavigateToRatesTimeSeries,
                onNavigateToCurrencySelection = onNavigateToCurrencySelection
            )
        },
    ) {
        CurrencyRatesSection(
            modifier = Modifier.screenHorizontalPadding(),
            onNavigateToCurrencySelection = onNavigateToCurrencySelection,
            onNavigateToCurrencyDetail = onNavigateToCurrencyDetail
        )
    }
}

@Composable
fun HomeScreenToolbar(
    modifier: Modifier = Modifier,
    onNavigateToRatesTimeSeries: () -> Unit,
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?,
        resultCallback: (String?) -> Unit
    ) -> Unit,
) {
    Column(
        modifier = modifier
            .screenHorizontalPadding()
            .padding(
                bottom = Dimens.spacingMedium()
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            ThemeToggleSection(
                modifier = Modifier
            )

            SpacerHorizontalMedium()

            RatesTimeSeriesButton(
                onClick = onNavigateToRatesTimeSeries
            )
        }

        SpacerVertMedium()

        CurrencyConversionSection(
            onNavigateToCurrencySelection = onNavigateToCurrencySelection,
        )
    }
}


@Composable
fun RatesTimeSeriesButton(
    modifier: Modifier = Modifier, onClick: () -> Unit
) {
    IconButton(
        modifier = modifier.size(45.dp), onClick = onClick
    ) {
        Icon(
            modifier = Modifier.padding(5.dp),
            painter = painterResource(id = R.drawable.ic_bar_chart),
            contentDescription = null
        )
    }
}

@Preview
@Composable
fun RatesTimeSeriesButtonPreview() {
    RatesTimeSeriesButton(onClick = {})
}



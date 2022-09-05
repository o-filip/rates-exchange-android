package com.ofilip.exchange_rates.ui.screen.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ofilip.exchange_rates.ui.component.button.SpacerVertMedium
import com.ofilip.exchange_rates.ui.extension.screenHorizontalPadding
import com.ofilip.exchange_rates.ui.navigation.DefaultDest
import com.ofilip.exchange_rates.ui.navigation.Dest
import com.ofilip.exchange_rates.ui.screen.home.component.currencyConversion.CurrencyConversionSection
import com.ofilip.exchange_rates.ui.screen.home.component.currencyRates.CurrencyRatesSection
import com.ofilip.exchange_rates.ui.screen.home.component.themeToggle.ThemeToggleSection
import com.ofilip.exchange_rates.ui.util.Dimens
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

object HomeScreenDest : Dest by DefaultDest("home") {
    fun path(): String = "home"
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToCurrencySelection: () -> Unit,
    onNavigateToSelectConversionCurrencyFrom: () -> Unit,
    onNavigateToSelectConversionCurrencyTo: () -> Unit,
    onNavigateToCurrencyDetail: (currencyCode: String) -> Unit
) {
    CollapsingToolbarScaffold(
        state = rememberCollapsingToolbarScaffoldState(),
        modifier = modifier,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            Column(modifier = Modifier
                .screenHorizontalPadding()
                .padding(
                    bottom = Dimens.spacingMedium()
                )) {
                ThemeToggleSection(
                    modifier = Modifier
                        .fillMaxWidth()
                )

                SpacerVertMedium()

                CurrencyConversionSection(
                    onSelectConversionCurrencyFrom = onNavigateToSelectConversionCurrencyFrom,
                    onSelectConversionCurrencyTo = onNavigateToSelectConversionCurrencyTo
                )
            }

        },
    ) {
        CurrencyRatesSection(
            modifier = Modifier.screenHorizontalPadding(),
            onNavigateToCurrencySelection = onNavigateToCurrencySelection,
            onNavigateToCurrencyDetail = onNavigateToCurrencyDetail
        )
    }
}



package com.ofilip.exchange_rates.ui.screen.currencySelection

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.ui.component.button.ArrowNavBack
import com.ofilip.exchange_rates.ui.component.button.SpacerVertMedium
import com.ofilip.exchange_rates.ui.extension.screenHorizontalPadding
import com.ofilip.exchange_rates.ui.navigation.DefaultDest
import com.ofilip.exchange_rates.ui.navigation.Dest
import com.ofilip.exchange_rates.ui.navigation.encodeToNavPath
import com.ofilip.exchange_rates.ui.screen.currencySelection.component.CurrencyFilterSection
import com.ofilip.exchange_rates.ui.screen.currencySelection.component.CurrencySelectionListItem
import com.ofilip.exchange_rates.ui.theme.ExchangeRatesTheme
import com.ofilip.exchange_rates.ui.util.Dimens

object CurrencySelectionScreenDest :
    Dest by DefaultDest("currencySelection/{preselectedCurrencies}") {

    fun path(preselectedCurrencies: List<String>): String =
        "currencySelection/${preselectedCurrencies.encodeToNavPath()}"

    override val arguments: List<NamedNavArgument> =
        listOf(
            navArgument("preselectedCurrencies") { type = NavType.StringType }
        )
}

@Composable
fun CurrencySelectionScreen(
    modifier: Modifier = Modifier,
    viewModel: CurrencySelectionViewModel = hiltViewModel(),
    onNavigateBack: (selectedCurrency: String?) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(uiState.userSelectedCurrency) {
        if (uiState.userSelectedCurrency != null) {
            onNavigateBack(uiState.userSelectedCurrency)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.init()
    }

    CurrencySelectionScreenContent(modifier = modifier,
        uiState = uiState,
        onNavigateBack = { onNavigateBack(null) },
        onQueryUpdated = { viewModel.onQueryUpdated(it) },
        onToggleShowFavoritesOnly = { viewModel.toggleShowOnlyFavorites() },
        onCurrencySelected = { viewModel.onCurrencySelected(it) },
        onCurrencyLikeToggled = { viewModel.toggleCurrencyLike(it) })
}

@Composable
fun CurrencySelectionScreenContent(
    modifier: Modifier = Modifier,
    uiState: CurrencySelectionUiState,
    onNavigateBack: () -> Unit,
    onQueryUpdated: (TextFieldValue) -> Unit,
    onToggleShowFavoritesOnly: () -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    onCurrencyLikeToggled: (Currency) -> Unit
) {
    Scaffold(modifier = modifier, topBar = {
        TopAppBar(elevation = 0.dp, title = {
            Text(text = stringResource(id = R.string.currency_selection_title))
        }, navigationIcon = {
            ArrowNavBack(onClick = { onNavigateBack() })
        }, backgroundColor = MaterialTheme.colors.background
        )
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .screenHorizontalPadding()
                .padding(top = Dimens.spacingMedium())
        ) {
            CurrencyFilterSection(
                textQuery = uiState.query,
                onQueryUpdated = onQueryUpdated,
                showFavoritesOnly = uiState.showOnlyFavorites,
                onToggleShowFavoritesOnly = onToggleShowFavoritesOnly,
                errorMessage = uiState.filteringErrorMessage
            )

            SpacerVertMedium()

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    uiState.currencies,
                ) { currency ->
                    CurrencySelectionListItem(modifier = Modifier.padding(vertical = 4.dp),
                        currency = currency,
                        isSelected = uiState.selectedCurrencies?.contains(currency.currencyCode)
                            ?: false,
                        onSelected = { onCurrencySelected(currency) },
                        onFavoriteToggle = { onCurrencyLikeToggled(currency) })
                }
            }
        }
    }
}

@Preview
@Composable
fun CurrencySelectionScreenContentPreviewLight() {
    ExchangeRatesTheme {
        CurrencySelectionScreenContent(uiState = CurrencySelectionUiState(
            currencies = listOf(
                Currency(
                    currencyCode = "USD",
                    currencyName = "United States Dollar",
                    isFavorite = false,
                    numberCode = "840",
                    precision = 2,
                    symbol = "$",
                    decimalSeparator = ".",
                    symbolFirst = true,
                    thousandsSeparator = ","
                )
            ),
            query = TextFieldValue(),
            showOnlyFavorites = false,
            selectedCurrencies = null,
            filteringErrorMessage = null,
            userSelectedCurrency = null
        ),
            onNavigateBack = { },
            onQueryUpdated = { },
            onToggleShowFavoritesOnly = { },
            onCurrencySelected = { },
            onCurrencyLikeToggled = { })
    }
}

@Preview
@Composable
fun CurrencySelectionScreenContentPreviewDark() {
    ExchangeRatesTheme(darkTheme = true) {
        CurrencySelectionScreenContent(uiState = CurrencySelectionUiState(
            currencies = listOf(
                Currency(
                    currencyCode = "USD",
                    currencyName = "United States Dollar",
                    isFavorite = false,
                    numberCode = "840",
                    precision = 2,
                    symbol = "$",
                    decimalSeparator = ".",
                    symbolFirst = true,
                    thousandsSeparator = ","
                )
            ),
            query = TextFieldValue(),
            showOnlyFavorites = false,
            selectedCurrencies = null,
            filteringErrorMessage = null,
            userSelectedCurrency = null
        ),
            onNavigateBack = { },
            onQueryUpdated = { },
            onToggleShowFavoritesOnly = { },
            onCurrencySelected = { },
            onCurrencyLikeToggled = { })
    }
}
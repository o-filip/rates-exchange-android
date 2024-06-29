package com.ofilip.exchange_rates.ui.screen.ratesTimeSeries.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.component.bottomSheet.DateRangePickerBottomSheetContent
import com.ofilip.exchange_rates.ui.component.button.CurrencySelectionButton

import com.ofilip.exchange_rates.ui.component.field.CustomInputChip
import com.ofilip.exchange_rates.ui.screen.ratesTimeSeries.RatesTimeSeriesDateRange
import com.ofilip.exchange_rates.ui.util.Dimens
import org.joda.time.DateTime


@Composable
fun RatesTimeSeriesFilter(
    modifier: Modifier = Modifier,
    baseCurrency: String,
    dateRange: RatesTimeSeriesDateRange,
    currency: String,
    onBaseCurrencyChange: (String) -> Unit,
    onDateRangeChanged: (RatesTimeSeriesDateRange) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?, resultCallback: (String?) -> Unit
    ) -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.screenHorizontalPadding())
    ) {
        RatesTimeSeriesFilterContent(
            modifier = modifier.padding(
                horizontal = Dimens.cardHorizontalPadding(),
                vertical = Dimens.cardVerticalPadding()
            ),
            baseCurrency = baseCurrency,
            dateRange = dateRange,
            currency = currency,
            onDateRangeChanged = onDateRangeChanged,
            onBaseCurrencyChange = onBaseCurrencyChange,
            onCurrencyChange = onCurrencyChange,
            onNavigateToCurrencySelection = onNavigateToCurrencySelection,
        )
    }
}

@Composable
fun RatesTimeSeriesFilterContent(
    modifier: Modifier = Modifier,
    baseCurrency: String,
    dateRange: RatesTimeSeriesDateRange,
    currency: String,
    onDateRangeChanged: (RatesTimeSeriesDateRange) -> Unit,
    onBaseCurrencyChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?, resultCallback: (String?) -> Unit
    ) -> Unit,
) {
    Column(modifier = modifier) {
        RatesTimeSeriesDateRangeSelection(
            dateRange = dateRange,
            onDateChanged = { dateRange ->
                onDateRangeChanged(dateRange)
            },
        )

        Divider(modifier = Modifier.padding(bottom = Dimens.spacingSmall()))

        RatesTimeSeriesFilterCurrenciesSection(
            baseCurrency = baseCurrency,
            currency = currency,
            onBaseCurrencyChange = onBaseCurrencyChange,
            onCurrencyChange = onCurrencyChange,
            onNavigateToCurrencySelection = onNavigateToCurrencySelection,
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RatesTimeSeriesDateRangeSelection(
    modifier: Modifier = Modifier,
    dateRange: RatesTimeSeriesDateRange,
    onDateChanged: (RatesTimeSeriesDateRange) -> Unit,
) {
    var showCustomDateSelectionDialog by remember {
        mutableStateOf(false)
    }

    Column {
        Text(
            text = stringResource(id = R.string.rate_time_series_filter_date_range_header),
            style = MaterialTheme.typography.h4
        )
        FlowRow(
            modifier = modifier
        ) {
            CustomInputChip(text = stringResource(id = R.string.rate_time_series_date_range_last_month),
                isSelected = dateRange is RatesTimeSeriesDateRange.LastWeek,
                onOptionSelected = { onDateChanged(RatesTimeSeriesDateRange.LastWeek) })

            CustomInputChip(text = stringResource(id = R.string.rate_time_series_date_range_last_month),
                isSelected = dateRange is RatesTimeSeriesDateRange.LastMonth,
                onOptionSelected = { onDateChanged(RatesTimeSeriesDateRange.LastMonth) })

            CustomInputChip(text = stringResource(id = R.string.rate_time_series_date_range_last_three_months),
                isSelected = dateRange is RatesTimeSeriesDateRange.LastThreeMonths,
                onOptionSelected = { onDateChanged(RatesTimeSeriesDateRange.LastThreeMonths) })

            CustomInputChip(
                text = stringResource(id = R.string.rate_time_series_date_range_custom),
                isSelected = dateRange is RatesTimeSeriesDateRange.Custom,
                onOptionSelected = { showCustomDateSelectionDialog = true }
            )
        }
    }

    if (showCustomDateSelectionDialog) {
        val (rangeFrom, rangeTo) = if (dateRange is RatesTimeSeriesDateRange.Custom) {
            dateRange.from to dateRange.to
        } else {
            null to null
        }

        DateRangePickerBottomSheetContent(
            onDismissRequest = { showCustomDateSelectionDialog = false },
            dateFrom = rangeFrom,
            dateTo = rangeTo,
            onDateRangeChanged = onDateChanged,
            isDateSelectable = {
                // Disable future dates and dates 3 months in the past
                val now = DateTime.now()
                it.isBefore(now) && it.isAfter(now.minusMonths(3))
            }
        )
    }

}

@Composable
fun RatesTimeSeriesFilterCurrenciesSection(
    modifier: Modifier = Modifier,
    baseCurrency: String,
    currency: String,
    onBaseCurrencyChange: (String) -> Unit,
    onCurrencyChange: (String) -> Unit,
    onNavigateToCurrencySelection: (
        preselectedCurrency: String?, resultCallback: (String?) -> Unit
    ) -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier.padding(bottom = Dimens.spacingSmall()),
            text = stringResource(id = R.string.rate_time_series_filter_currencies_header),
            style = MaterialTheme.typography.h4
        )

        CurrencySelectionButton(
            onClick = {
                onNavigateToCurrencySelection(baseCurrency) { result ->
                    result?.let { onBaseCurrencyChange(it) }
                }
            },
            currencyCode = baseCurrency,
            prefixText = "${stringResource(id = R.string.rate_time_series_filter_base_currency_prefix)}: "
        )

        CurrencySelectionButton(
            onClick = {
                onNavigateToCurrencySelection(currency) { result ->
                    result?.let { onCurrencyChange(it) }
                }
            },
            currencyCode = currency,
            prefixText = "${stringResource(id = R.string.rate_time_series_filter_compare_currency_prefix)}: "
        )
    }
}



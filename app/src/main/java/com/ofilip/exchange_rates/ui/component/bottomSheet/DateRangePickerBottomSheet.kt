package com.ofilip.exchange_rates.ui.component.bottomSheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ofilip.exchange_rates.ui.screen.ratesTimeSeries.RatesTimeSeriesDateRange
import org.joda.time.DateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerBottomSheetContent(
    onDismissRequest: () -> Unit,
    dateFrom: DateTime?,
    dateTo: DateTime?,
    onDateRangeChanged: (RatesTimeSeriesDateRange) -> Unit,
    isDateSelectable: (DateTime) -> Boolean = { true }
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        DateRangePickerBottomSheetContent(
            dateFrom = dateFrom,
            dateTo = dateTo,
            onDateRangeSelected = { dateFrom, dateTo ->
                onDateRangeChanged(RatesTimeSeriesDateRange.Custom(dateFrom, dateTo))
                onDismissRequest()
            },
            isDateSelectable = isDateSelectable
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerBottomSheetContent(
    modifier: Modifier = Modifier,
    dateFrom: DateTime?,
    dateTo: DateTime?,
    onDateRangeSelected: (dateFrom: DateTime, dateTo: DateTime) -> Unit,
    isDateSelectable: (DateTime) -> Boolean = { true }
) {
    val dateRangePickerState = rememberDateRangePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                isDateSelectable(DateTime(utcTimeMillis))
        }
    )

    LaunchedEffect(dateFrom, dateTo) {
        dateRangePickerState.setSelection(
            dateFrom?.millis, dateTo?.millis
        )
    }

    Box(
        modifier = modifier
    ) {
        DateRangePicker(
            modifier = Modifier,
            state = dateRangePickerState,
            showModeToggle = false,
        )

        if (dateRangePickerState.selectedStartDateMillis != null
            && dateRangePickerState.selectedEndDateMillis != null
        ) {
            Button(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
                onClick = {
                    onDateRangeSelected(
                        DateTime(dateRangePickerState.selectedStartDateMillis!!),
                        DateTime(dateRangePickerState.selectedEndDateMillis!!)
                    )
                }) {
                Text("Confirm")
            }
        }
    }
}

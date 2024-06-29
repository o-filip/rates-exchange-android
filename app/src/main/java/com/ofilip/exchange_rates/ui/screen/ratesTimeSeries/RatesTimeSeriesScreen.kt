package com.ofilip.exchange_rates.ui.screen.ratesTimeSeries

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ofilip.exchange_rates.ui.navigation.DefaultDest
import com.ofilip.exchange_rates.ui.navigation.Dest

object RatesTimeSeriesScreenDest : Dest by DefaultDest(route = "ratesTimeSeries") {
    fun path(): String = "ratesTimeSeries"
}

@Composable
fun RatesTimeSeriesScreen(
    modifier: Modifier = Modifier,
    viewModel: RatesTimeSeriesViewModel = hiltViewModel()
) {

    Scaffold(
        modifier = modifier
    ) {

    }
}
package com.ofilip.exchange_rates.core.entity

import org.joda.time.DateTime

data class RatesTimeSeriesItem(
    val date: DateTime,
    /**
     * Map of currency code to exchange rate.
     */
    val rates: Map<String, Double?>
)

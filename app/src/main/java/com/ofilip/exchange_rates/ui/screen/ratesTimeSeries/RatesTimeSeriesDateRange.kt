package com.ofilip.exchange_rates.ui.screen.ratesTimeSeries

import org.joda.time.DateTime

sealed class RatesTimeSeriesDateRange {
    data class Custom(
        override val from: DateTime,
        override val to: DateTime
    ) : RatesTimeSeriesDateRange()

    data object LastWeek : RatesTimeSeriesDateRange() {
        override val to: DateTime by lazy { DateTime.now() }
        override val from: DateTime by lazy { to.minusWeeks(1) }
    }

    data object LastMonth : RatesTimeSeriesDateRange() {
        override val to: DateTime by lazy { DateTime.now() }
        override val from: DateTime by lazy { to.minusMonths(1) }
    }

    data object LastThreeMonths : RatesTimeSeriesDateRange() {
        override val to: DateTime by lazy { DateTime.now() }
        override val from: DateTime by lazy { to.minusMonths(3) }
    }

    abstract val from: DateTime
    abstract val to: DateTime
}



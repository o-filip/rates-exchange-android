package com.ofilip.exchange_rates.ui.util

import co.yml.charts.common.model.Point
import com.ofilip.exchange_rates.core.entity.RatesTimeSeriesItem
import javax.inject.Inject
import org.joda.time.DateTime

/**
 * Data model for the chart
 *
 * @param dataPoints list of data points for each currency
 * @param initialDate date of the first data point
 */
data class ChartDataModel(
    val dataPoints: List<Point>,
    val initialDate: DateTime,
) {
    val minRateValue by lazy { dataPoints.minOf { it.y } }

    val maxRateValue by lazy { dataPoints.maxOf { it.y } }

    val hasUnchangedRate =
        dataPoints.isNotEmpty() && dataPoints.all { it.y == dataPoints.first().y }
}

interface ChartHelper {
    fun convertToChartDataModel(
        currencyCode: String,
        source: List<RatesTimeSeriesItem>
    ): ChartDataModel
}

class ChartHelperImpl @Inject constructor() : ChartHelper {
    override fun convertToChartDataModel(
        currencyCode: String,
        source: List<RatesTimeSeriesItem>
    ): ChartDataModel {
        val points = source.mapIndexed { index, ratesTimeSeriesItem ->
            val rate = ratesTimeSeriesItem.rates[currencyCode]
            if (rate != null) {
                Point(index.toFloat(), rate.toFloat())
            } else {
                null
            }
        }.filterNotNull()

        return ChartDataModel(
            dataPoints = points,
            initialDate = source.first().date
        )
    }
}

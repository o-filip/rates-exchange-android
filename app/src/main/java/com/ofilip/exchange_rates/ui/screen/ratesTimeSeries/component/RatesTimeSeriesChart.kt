package com.ofilip.exchange_rates.ui.screen.ratesTimeSeries.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.ui.extension.screenHorizontalPadding
import com.ofilip.exchange_rates.ui.util.ChartDataModel
import com.ofilip.exchange_rates.ui.util.Dimens
import com.ofilip.exchange_rates.ui.util.formatShotDateWithOffset
import com.valentinilk.shimmer.shimmer

private data class StateWrapper(
    val isLoading: Boolean,
    val chartData: ChartDataModel?,
    val errorMessage: String?
)

@Composable
fun RatesTimeSeriesChart(
    modifier: Modifier = Modifier,
    data: ChartDataModel?,
    errorMessage: String?,
    isLoading: Boolean
) {

    Card(
        modifier = modifier
            .fillMaxHeight()
            .height(300.dp)
            .screenHorizontalPadding()
    ) {
        AnimatedContent(
            targetState = StateWrapper(isLoading, data, errorMessage),
            label = "RatesTimeSeriesChartContent",
            contentKey = {
                when {
                    it.isLoading && it.chartData == null -> "InitialLoading"
                    it.errorMessage != null -> "ErrorMessage"
                    it.chartData != null -> if (it.chartData.hasUnchangedRate) "UnchangedRateMessage" else "LineChart"
                    else -> "InitialLoading"
                }
            }) {
            when {
                it.isLoading && data == null -> InitialLoading()

                it.errorMessage != null -> ErrorMessage(
                    errorMessage = it.errorMessage
                )

                it.chartData != null ->
                    if (it.chartData.hasUnchangedRate) {
                        UnchangedRateMessage(
                            value = it.chartData.dataPoints.first().y
                        )
                    } else {
                        LineChart(
                            modifier = Modifier
                                .fillMaxSize(),
                            lineChartData = createLineaChartData(data = it.chartData)
                        )
                    }
            }
        }
    }
}

@Composable
private fun InitialLoading() {
    Icon(
        modifier = Modifier
            .padding(120.dp)
            .shimmer(),
        painter = painterResource(id = R.drawable.ic_bar_chart),
        contentDescription = null
    )
}

@Composable
private fun ErrorMessage(
    modifier: Modifier = Modifier,
    errorMessage: String
) {
    Box(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(
                    horizontal = Dimens.cardHorizontalPadding(),
                    vertical = Dimens.cardVerticalPadding()
                ),
            text = errorMessage,
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun UnchangedRateMessage(
    value: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(
                id = R.string.rates_time_series_unchanged_rates_message,
                "%.2f".format(value)
            ),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun createLineaChartData(
    data: ChartDataModel
): LineChartData {
    // Get width of chart without axis and padding
    val width =
        LocalConfiguration.current.screenWidthDp.dp - 100.dp - Dimens.screenHorizontalPadding() * 2

    val axisLabelCount = 5

    val xAxisData = rememberAxisData(
        dataPointsSize = data.dataPoints.size,
        axisStepSize = width / data.dataPoints.size.toFloat(),
        steps = axisLabelCount,
        labelData = { i ->
            if (i == 0 || i >= data.dataPoints.size) {
                ""
            } else {
                data.initialDate.formatShotDateWithOffset(i)
            }
        },
        bottomPadding = 15.dp,
    )

    val yAxisData = rememberAxisData(
        dataPointsSize = data.dataPoints.size,
        steps = axisLabelCount,
        labelData = { i ->
            val yScale = (data.maxRateValue - data.minRateValue) / axisLabelCount
            "%.2f".format((i.toFloat() * yScale) + data.minRateValue)
        },
    )

    val linePlotData = LinePlotData(
        lines = listOf(
            Line(
                dataPoints = data.dataPoints,
                rememberLineStyle(color = MaterialTheme.colors.primary),
                null,
                rememberSelectionHighlightPoint(
                    color = MaterialTheme.colors.primary
                ),
                rememberShadowUnderLine(
                    color = MaterialTheme.colors.primary.copy(alpha = 0.5f)
                ),
                rememberSelectionHighlightPopUp(
                    backgroundColor = MaterialTheme.colors.surface,
                    labelColor = MaterialTheme.colors.onSurface,
                ) { x, y ->
                    "%.2f - %s".format(y, data.initialDate.formatShotDateWithOffset(x.toInt()))
                }
            )
        )
    )

    return LineChartData(
        linePlotData = linePlotData,
        backgroundColor = MaterialTheme.colors.surface,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = rememberGridLines(
            color = MaterialTheme.colors.onSurface
        ),
        paddingRight = 0.dp,
        bottomPadding = 15.dp,
        isZoomAllowed = false
    )
}

@Composable
fun rememberAxisData(
    dataPointsSize: Int,
    axisLabelColor: Color = MaterialTheme.colors.onSurface,
    axisLineColor: Color = MaterialTheme.colors.onSurface,
    backgroundColor: Color = MaterialTheme.colors.surface,
    axisStepSize: Dp? = null,
    steps: Int? = null,
    labelData: ((Int) -> String)? = null,
    bottomPadding: Dp? = null,
): AxisData = remember(
    dataPointsSize,
    axisLabelColor,
    axisLineColor,
    backgroundColor,
    axisStepSize,
    steps,
    labelData,
    bottomPadding
) {
    AxisData.Builder()
        .axisLabelColor(axisLabelColor)
        .axisLineColor(axisLineColor)
        .backgroundColor(backgroundColor)
        .apply {
            if (axisStepSize != null) axisStepSize(axisStepSize)
            if (steps != null) steps(steps)
            if (labelData != null) labelData(labelData)
            if (bottomPadding != null) bottomPadding(bottomPadding)
        }
        .build()
}


@Composable
fun rememberLineStyle(
    color: Color
): LineStyle = remember(color) {
    LineStyle(
        color = color
    )
}

@Composable
fun rememberSelectionHighlightPoint(
    color: Color
): SelectionHighlightPoint = remember(color) {
    SelectionHighlightPoint(
        color = color
    )
}

@Composable
fun rememberShadowUnderLine(
    color: Color
): ShadowUnderLine = remember(color) {
    ShadowUnderLine(
        color = color
    )
}

@Composable
fun rememberSelectionHighlightPopUp(
    backgroundColor: Color,
    labelColor: Color,
    popUpLabel: (Float, Float) -> String
): SelectionHighlightPopUp = remember(backgroundColor, labelColor, popUpLabel) {
    SelectionHighlightPopUp(
        backgroundColor = backgroundColor,
        labelColor = labelColor,
        popUpLabel = popUpLabel
    )
}

@Composable
fun rememberGridLines(
    color: Color
): GridLines = remember(color) {
    GridLines(
        color = color
    )
}

package com.ofilip.exchange_rates.ui.screen.ratesTimeSeries

import com.ofilip.exchange_rates.core.entity.RatesTimeSeriesItem
import com.ofilip.exchange_rates.domain.useCase.rateTimeSeries.GetRatesTimeSeriesUseCase
import com.ofilip.exchange_rates.ui.util.ChartDataModel
import com.ofilip.exchange_rates.ui.util.ChartHelper
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.joda.time.DateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class RatesTimeSeriesViewStateWrapperTest {
    private val mockGetRatesTimeSeriesUseCase: GetRatesTimeSeriesUseCase = mock()
    private val mockUiErrorConverter: UiErrorConverter = mock()
    private val mockChartHelper: ChartHelper = mock()
    private val testCoroutineScope: TestDispatcher = StandardTestDispatcher()

    private fun createViewModel() = RatesTimeSeriesViewModel(
        getRatesTimeSeriesUseCase = mockGetRatesTimeSeriesUseCase,
        uiErrorConverter = mockUiErrorConverter,
        chartHelper = mockChartHelper
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testCoroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads rates time series`() = runTest {
        // Given
        val mockRatesTimeSeries = listOf(
            RatesTimeSeriesItem(DateTime.parse("2023-01-01"), mapOf("CZK" to 22.0)),
            RatesTimeSeriesItem(DateTime.parse("2023-01-02"), mapOf("CZK" to 21.5)),
            RatesTimeSeriesItem(DateTime.parse("2023-01-03"), mapOf("CZK" to 21.7))
        )
        val mockChartData = ChartDataModel(
            listOf(),
            initialDate = DateTime.parse("2023-01-01"),
        )

        mockGetRatesTimeSeriesUseCase.stub {
            onBlocking {
                execute(
                    any(), any(), any(), any()
                )
            } doReturn mockRatesTimeSeries
        }
        mockChartHelper.stub {
            on { convertToChartDataModel(any(), any()) } doReturn mockChartData
        }

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        verify(mockGetRatesTimeSeriesUseCase).execute(any(), any(), any(), any())
        assertEquals(mockChartData, viewModel.uiState.value.chartData)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `onDateRangeChanged updates state and reloads rates`() = runTest {
        // Given
        val newDateRange = RatesTimeSeriesDateRange.LastMonth
        val mockRatesTimeSeries = listOf(
            RatesTimeSeriesItem(DateTime.parse("2023-01-01"), mapOf("CZK" to 22.0)),
            RatesTimeSeriesItem(DateTime.parse("2023-01-02"), mapOf("CZK" to 21.5)),
            RatesTimeSeriesItem(DateTime.parse("2023-01-03"), mapOf("CZK" to 21.7))
        )
        val mockChartData = ChartDataModel(
            listOf(),
            initialDate = DateTime.parse("2023-01-01"),
        )

        mockGetRatesTimeSeriesUseCase.stub {
            onBlocking {
                execute(
                    any(), any(), any(), any()
                )
            } doReturn mockRatesTimeSeries
        }
        mockChartHelper.stub {
            on { convertToChartDataModel(any(), any()) } doReturn mockChartData
        }

        // When
        val viewModel = createViewModel()
        viewModel.onDateRangeChanged(newDateRange)
        advanceUntilIdle()

        // Then
        verify(mockGetRatesTimeSeriesUseCase, times(2)).execute(any(), any(), any(), any())
        assertEquals(newDateRange, viewModel.uiState.value.dateRange)
        assertEquals(mockChartData, viewModel.uiState.value.chartData)
    }

    @Test
    fun `loadRatesTimeSeries sets error message on failure`() = runTest {
        // Given
        val exception = RuntimeException("Failed to load rates")
        val errorMessage = "Error loading rates"

        mockGetRatesTimeSeriesUseCase.stub {
            onBlocking {
                execute(
                    any(), any(), any(), any()
                )
            } .thenThrow(exception)
        }
        mockUiErrorConverter.stub {
            on { convertToText(exception) } doReturn errorMessage
        }

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        verify(mockGetRatesTimeSeriesUseCase).execute(any(), any(), any(), any())
        verify(mockUiErrorConverter).convertToText(exception)
        assertEquals(errorMessage, viewModel.uiState.value.ratesTimeSeriesErrorMessage)
        assertEquals(false, viewModel.uiState.value.isLoading)
    }

    @Test
    fun `onBaseCurrencyCodeChanged updates state and reloads rates`() = runTest {
        // Given
        val newBaseCurrencyCode = "USD"
        val mockRatesTimeSeries = listOf(
            RatesTimeSeriesItem(DateTime.parse("2023-01-01"), mapOf("CZK" to 22.0)),
            RatesTimeSeriesItem(DateTime.parse("2023-01-02"), mapOf("CZK" to 21.5)),
            RatesTimeSeriesItem(DateTime.parse("2023-01-03"), mapOf("CZK" to 21.7))
        )
        val mockChartData = ChartDataModel(
            listOf(),
            initialDate = DateTime.parse("2023-01-01"),
        )

        mockGetRatesTimeSeriesUseCase.stub {
            onBlocking {
                execute(
                    any(), any(), any(), any()
                )
            } doReturn mockRatesTimeSeries
        }
        mockChartHelper.stub {
            on { convertToChartDataModel(any(), any()) } doReturn mockChartData
        }

        // When
        val viewModel = createViewModel()
        viewModel.onBaseCurrencyCodeChanged(newBaseCurrencyCode)
        advanceUntilIdle()

        // Then
        verify(mockGetRatesTimeSeriesUseCase, times(2)).execute(any(), any(), any(), any())
        assertEquals(newBaseCurrencyCode, viewModel.uiState.value.baseCurrencyCode)
        assertEquals(mockChartData, viewModel.uiState.value.chartData)
    }

    @Test
    fun `onCurrencyCodeChanged updates state and reloads rates`() = runTest {
        // Given
        val newCurrencyCode = "CZK"
        val mockRatesTimeSeries = listOf(
            RatesTimeSeriesItem(DateTime.parse("2023-01-01"), mapOf("CZK" to 22.0)),
            RatesTimeSeriesItem(DateTime.parse("2023-01-02"), mapOf("CZK" to 21.5)),
            RatesTimeSeriesItem(DateTime.parse("2023-01-03"), mapOf("CZK" to 21.7))
        )
        val mockChartData = ChartDataModel(
            listOf(),
            initialDate = DateTime.parse("2023-01-01"),
        )

        mockGetRatesTimeSeriesUseCase.stub {
            onBlocking {
                execute(
                    any(), any(), any(), any()
                )
            } doReturn mockRatesTimeSeries
        }
        mockChartHelper.stub {
            on { convertToChartDataModel(any(), any()) } doReturn mockChartData
        }

        // When
        val viewModel = createViewModel()
        viewModel.onCurrencyCodeChanged(newCurrencyCode)
        advanceUntilIdle()

        // Then
        verify(mockGetRatesTimeSeriesUseCase, times(2)).execute(any(), any(), any(), any())
        assertEquals(newCurrencyCode, viewModel.uiState.value.currencyCode)
        assertEquals(mockChartData, viewModel.uiState.value.chartData)
    }
}

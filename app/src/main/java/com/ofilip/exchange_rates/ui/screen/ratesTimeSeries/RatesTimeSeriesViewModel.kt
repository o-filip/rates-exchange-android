package com.ofilip.exchange_rates.ui.screen.ratesTimeSeries


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.core.extensions.onEither
import com.ofilip.exchange_rates.domain.useCase.rateTimeSeries.GetRatesTimeSeriesUseCase
import com.ofilip.exchange_rates.ui.util.ChartDataModel
import com.ofilip.exchange_rates.ui.util.ChartHelper
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RatesTimeSeriesUiState(
    val isLoading: Boolean = false,
    val dateRange: RatesTimeSeriesDateRange = RatesTimeSeriesDateRange.LastMonth,
    val baseCurrencyCode: String = "USD",
    val currencyCode: String = "CZK",
    val ratesTimeSeriesErrorMessage: String? = null,
    val chartData: ChartDataModel? = null,
)

@HiltViewModel
class RatesTimeSeriesViewModel @Inject constructor(
    private val getRatesTimeSeriesUseCase: GetRatesTimeSeriesUseCase,
    private val uiErrorConverter: UiErrorConverter,
    private val chartHelper: ChartHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(RatesTimeSeriesUiState())
    val uiState: StateFlow<RatesTimeSeriesUiState> get() = _uiState

    fun init() {
        loadRatesTimeSeries()
    }

    fun onDateRangeChanged(dateRange: RatesTimeSeriesDateRange) {
        _uiState.value = _uiState.value.copy(
            dateRange = dateRange,
        )
        loadRatesTimeSeries()
    }

    fun onBaseCurrencyCodeChanged(baseCurrencyCode: String) {
        _uiState.value = _uiState.value.copy(baseCurrencyCode = baseCurrencyCode)
        loadRatesTimeSeries()
    }

    fun onCurrencyCodeChanged(currencyCode: String) {
        _uiState.value = _uiState.value.copy(
            currencyCode = currencyCode
        )
        loadRatesTimeSeries()
    }


    private fun loadRatesTimeSeries() {
        val uiState = _uiState.value

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                ratesTimeSeriesErrorMessage = null,
                isLoading = true
            )

            getRatesTimeSeriesUseCase.execute(
                startDate = uiState.dateRange.from,
                endDate = uiState.dateRange.to,
                baseCurrencyCode = uiState.baseCurrencyCode,
                currencyCode = uiState.currencyCode
            ).onSuccess {
                _uiState.value = _uiState.value.copy(
                    chartData = chartHelper.convertToChartDataModel(uiState.currencyCode, it),
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    ratesTimeSeriesErrorMessage = uiErrorConverter.convertToText(it)
                )
            }.onEither {
                _uiState.value = _uiState.value.copy(
                    isLoading = false
                )
            }
        }
    }
}

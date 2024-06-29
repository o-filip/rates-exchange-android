package com.ofilip.exchange_rates.ui.screen.ratesTimeSeries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.core.entity.RatesTimeSeriesItem
import com.ofilip.exchange_rates.core.error.UiError
import com.ofilip.exchange_rates.core.extensions.onEither
import com.ofilip.exchange_rates.domain.useCase.GetRatesTimeSeriesUseCase
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.Duration

data class RatesTimeSeriesUiState(
    val isLoading: Boolean = false,
    val startDate: DateTime = DateTime.now().minus(Duration.standardDays(7)),
    val endDate: DateTime = DateTime.now(),
    val baseCurrencyCode: String = "USD",
    val currencyCodes: List<String> = listOf("EUR", "CZK"),
    val validationErrorMessage: String? = null,
    val ratesTimeSeriesErrorMessage: String? = null,
    val ratesTimeSeries: List<RatesTimeSeriesItem> = emptyList()
)

@HiltViewModel
class RatesTimeSeriesViewModel @Inject constructor(
    private val getRatesTimeSeriesUseCase: GetRatesTimeSeriesUseCase,
    private val uiErrorConverter: UiErrorConverter
) : ViewModel() {

    private val _uiState = MutableStateFlow(RatesTimeSeriesUiState())
    val uiState: Flow<RatesTimeSeriesUiState> get() = _uiState

    fun onStartDateChanged(startDate: DateTime) {
        _uiState.value = _uiState.value.copy(startDate = startDate)
    }

    fun onEndDateChanged(endDate: DateTime) {
        _uiState.value = _uiState.value.copy(endDate = endDate)
    }

    fun onBaseCurrencyCodeChanged(baseCurrencyCode: String) {
        _uiState.value = _uiState.value.copy(baseCurrencyCode = baseCurrencyCode)
    }

    fun onCurrencyCodesChanged(currencyCodes: List<String>) {
        _uiState.value = _uiState.value.copy(currencyCodes = currencyCodes)
    }


    fun loadRatesTimeSeries() {
        val uiState = _uiState.value

        if (!validateInput()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = uiState.copy(
                ratesTimeSeriesErrorMessage = null,
                validationErrorMessage = null,
                isLoading = true
            )

            getRatesTimeSeriesUseCase.execute(
                startDate = uiState.startDate,
                endDate = uiState.endDate,
                baseCurrencyCode = uiState.baseCurrencyCode,
                currencyCodes = uiState.currencyCodes
            ).onSuccess {
                _uiState.value = uiState.copy(
                    ratesTimeSeries = it
                )
            }.onFailure {
                _uiState.value = uiState.copy(
                    ratesTimeSeriesErrorMessage = uiErrorConverter.convertToText(it)
                )
            }.onEither {
                _uiState.value = uiState.copy(
                    isLoading = false
                )
            }
        }
    }

    private fun validateInput(): Boolean {
        val uiState = _uiState.value
        var error: UiError? = null

        if (uiState.currencyCodes.isEmpty()) {
            error = UiError.NoCurrenciesSelected
        } else if (uiState.startDate.isAfter(uiState.endDate)) {
            error = UiError.StartDateIsAfterEndDate
        }

        return if (error != null) {
            _uiState.value = uiState.copy(
                validationErrorMessage = uiErrorConverter.convertToText(error)
            )
            false
        } else {
            true
        }

    }

}
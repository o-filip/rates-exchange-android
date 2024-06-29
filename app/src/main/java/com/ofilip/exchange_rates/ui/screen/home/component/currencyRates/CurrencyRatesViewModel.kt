package com.ofilip.exchange_rates.ui.screen.home.component.currencyRates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.core.extensions.collectIn
import com.ofilip.exchange_rates.domain.useCase.currency.GetOverviewBaseCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.SetOverviewBaseCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.rate.GetRatesForOverviewUseCase
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class CurrencyRatesUiState(
    val rates: List<CurrencyRate> = emptyList(),
    val ratesLoading: Boolean = false,
    val overviewCurrency: String? = null,
    val ratesLoadErrorMessage: String? = null,
    val baseCurrencyErrorMessage: String? = null
)

@HiltViewModel
class CurrencyRatesViewModel @Inject constructor(
    private val getOverviewRatesUseCase: GetRatesForOverviewUseCase,
    private val getOverviewBaseCurrencyUseCase: GetOverviewBaseCurrencyUseCase,
    private val uiErrorConverter: UiErrorConverter,
    private val setOverviewBaseCurrencyUseCase: SetOverviewBaseCurrencyUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyRatesUiState())
    val uiState: StateFlow<CurrencyRatesUiState> get() = _uiState

    private var isInitialized = false

    fun init() {
        if (isInitialized) return
        isInitialized = true

        loadRates()

        getOverviewBaseCurrencyUseCase.execute().collectIn(viewModelScope) { currency ->
            currency.onSuccess {
                _uiState.value = uiState.value.copy(
                    overviewCurrency = it
                )
            }.onFailure {
                _uiState.value = uiState.value.copy(
                    baseCurrencyErrorMessage = uiErrorConverter.convertToText(it),
                )
            }
        }
    }

    private fun loadRates() {
        viewModelScope.launch {
            _uiState.value = uiState.value.copy(
                ratesLoading = true,
                ratesLoadErrorMessage = null,
                baseCurrencyErrorMessage = null,
            )

            getOverviewRatesUseCase.execute().collect { result ->
                result.onSuccess { rates ->
                    _uiState.value = uiState.value.copy(
                        rates = rates,
                        ratesLoading = false
                    )
                }.onFailure {
                    _uiState.value = uiState.value.copy(
                        ratesLoading = false,
                        ratesLoadErrorMessage = uiErrorConverter.convertToText(it)
                    )
                }
            }
        }
    }

    fun refreshData() {
        loadRates()
    }

    fun onBaseCurrencySelected(currencyCode: String) {
        viewModelScope.launch {
            setOverviewBaseCurrencyUseCase.execute(currencyCode)
        }
    }

}

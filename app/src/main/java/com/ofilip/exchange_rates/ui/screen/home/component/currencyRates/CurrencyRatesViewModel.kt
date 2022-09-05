package com.ofilip.exchange_rates.ui.screen.home.component.currencyRates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.core.extensions.collectIn
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.domain.useCase.GetRatesForOverviewUseCase
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RatesOverviewUiState(
    val rates: List<CurrencyRate> = emptyList(),
    val ratesLoading: Boolean = false,
    val overviewCurrency: String? = null,
    val ratesLoadErrorMessage: String? = null,
    val baseCurrencyErrorMessage: String? = null
)

@HiltViewModel
class RatesOverviewViewModel @Inject constructor(
    private val getOverviewRatesUseCase: GetRatesForOverviewUseCase,
    private val currencyRepository: CurrencyRepository,
    private val uiErrorConverter: UiErrorConverter
) : ViewModel() {

    private val _uiState = MutableStateFlow(RatesOverviewUiState())
    val uiState: StateFlow<RatesOverviewUiState> get() = _uiState

    private var isInitialized = false

    fun init() {
        if (isInitialized) return
        isInitialized = true

        loadRates()

        currencyRepository.overviewBaseCurrency.collectIn(viewModelScope) { currency ->
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
                ratesLoadErrorMessage = null
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


}

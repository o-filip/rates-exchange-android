package com.ofilip.exchange_rates.ui.screen.currencyDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.core.extensions.collectIn
import com.ofilip.exchange_rates.domain.useCase.currency.GetCurrencyUseCase
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry

data class CurrencyDetailUiState(
    val isLoading: Boolean = false,
    val currency: Currency? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class CurrencyDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCurrencyUseCase: GetCurrencyUseCase,
    private val uiErrorConverter: UiErrorConverter,
) : ViewModel() {
    private val currencyCode: String = checkNotNull(savedStateHandle["currencyCode"])

    private val _uiState = MutableStateFlow(CurrencyDetailUiState())
    val uiState: StateFlow<CurrencyDetailUiState> get() = _uiState

    init {
        _uiState.value = uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        getCurrencyUseCase.execute(currencyCode)
            .catch {
                _uiState.value =
                    uiState.value.copy(
                        errorMessage = uiErrorConverter.convertToText(it),
                        isLoading = false
                    )
            }
            .collectIn(viewModelScope) { currency ->
                _uiState.value = uiState.value.copy(
                    currency = currency,
                    isLoading = false
                )
            }
    }
}

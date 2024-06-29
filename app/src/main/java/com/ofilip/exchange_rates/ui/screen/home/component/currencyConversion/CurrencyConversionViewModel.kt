package com.ofilip.exchange_rates.ui.screen.home.component.currencyConversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.core.error.UiError
import com.ofilip.exchange_rates.core.extensions.collectIn
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.domain.useCase.ConvertCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.SetConversionCurrencyFromUseCase
import com.ofilip.exchange_rates.domain.useCase.SetConversionCurrencyToUseCase
import com.ofilip.exchange_rates.ui.component.field.DecimalTextFieldValue
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class CurrencyConversionUiState(
    val convertAmountFrom: DecimalTextFieldValue = DecimalTextFieldValue.create(),
    val convertAmountTo: DecimalTextFieldValue = DecimalTextFieldValue.create(),
    val convertCurrencyFrom: String? = null,
    val convertCurrencyTo: String? = null,
    val convertCurrencyFromErrorMessage: String? = null,
    val convertCurrencyToErrorMessage: String? = null,
    val conversionErrorMessage: String? = null,
)

@HiltViewModel
class CurrencyConversionViewModel @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val uiErrorConverter: UiErrorConverter,
    currencyRepository: CurrencyRepository,
    private val setConversionCurrencyToUseCase: SetConversionCurrencyToUseCase,
    private val setConversionCurrencyFromUseCase: SetConversionCurrencyFromUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CurrencyConversionUiState())
    val uiState: StateFlow<CurrencyConversionUiState> get() = _uiState

    init {
        currencyRepository.conversionCurrencyFrom.collectIn(viewModelScope) { currency ->
            currency.onSuccess {
                _uiState.value = uiState.value.copy(
                    convertCurrencyFrom = it
                )
                onConversionCurrenciesChanged()
            }.onFailure {
                _uiState.value = uiState.value.copy(
                    convertCurrencyFrom = null,
                    convertCurrencyFromErrorMessage = uiErrorConverter.convertToText(it)
                )
            }
        }

        currencyRepository.conversionCurrencyTo.collectIn(viewModelScope) { currency ->
            currency.onSuccess {
                _uiState.value = uiState.value.copy(
                    convertCurrencyTo = it
                )
                onConversionCurrenciesChanged()
            }.onFailure {
                _uiState.value = uiState.value.copy(
                    convertCurrencyTo = null,
                    convertCurrencyToErrorMessage = uiErrorConverter.convertToText(it)
                )
            }
        }
    }

    fun onConvertAmountFromChanged(value: DecimalTextFieldValue) {
        viewModelScope.launch {
            convertAmount(
                value.number,
                uiState.value.convertCurrencyFrom,
                uiState.value.convertCurrencyTo
            ).onSuccess {
                _uiState.value = uiState.value.copy(
                    convertAmountFrom = value,
                    convertAmountTo = DecimalTextFieldValue.create(it),
                )
            }.onFailure {
                _uiState.value = uiState.value.copy(
                    conversionErrorMessage = uiErrorConverter.convertToText(it)
                )
            }
        }
    }

    fun onConvertAmountToChanged(value: DecimalTextFieldValue) {
        viewModelScope.launch {
            convertAmount(
                value.number,
                uiState.value.convertCurrencyTo,
                uiState.value.convertCurrencyFrom
            )
                .onSuccess {
                    _uiState.value = uiState.value.copy(
                        convertAmountFrom = DecimalTextFieldValue.create(it),
                        convertAmountTo = value,
                    )
                }.onFailure {
                    _uiState.value = uiState.value.copy(
                        conversionErrorMessage = uiErrorConverter.convertToText(it)
                    )
                }

        }
    }

    private suspend fun convertAmount(
        amount: Double?,
        fromCurrency: String?,
        toCurrency: String?
    ): Result<Double?> =
        if (amount == null) {
            Result.success(null)
        } else if (fromCurrency == null || toCurrency == null) {
            Result.failure(UiError.CurrencyNotSelected)
        } else {
            convertCurrencyUseCase.execute(
                amount,
                fromCurrency,
                toCurrency
            )
        }

    private suspend fun onConversionCurrenciesChanged() {
        uiState.value.let { state ->
            convertAmount(
                state.convertAmountFrom.number,
                state.convertCurrencyFrom,
                state.convertCurrencyTo
            ).onSuccess {
                _uiState.value = uiState.value.copy(
                    convertAmountTo = DecimalTextFieldValue.create(it),
                )
            }.onFailure {
                _uiState.value = uiState.value.copy(
                    conversionErrorMessage = uiErrorConverter.convertToText(it)
                )
            }
        }
    }

    fun setConversionCurrencyFrom(currency: String) {
        viewModelScope.launch {
            setConversionCurrencyFromUseCase.execute(currency)
        }
    }

    fun setConversionCurrencyTo(currency: String) {
        viewModelScope.launch {
            setConversionCurrencyToUseCase.execute(currency)
        }
    }
}

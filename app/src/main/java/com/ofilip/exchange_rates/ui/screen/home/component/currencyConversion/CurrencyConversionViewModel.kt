package com.ofilip.exchange_rates.ui.screen.home.component.currencyConversion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.core.extensions.collectIn
import com.ofilip.exchange_rates.domain.useCase.currency.GetConversionCurrencyFromUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.GetConversionCurrencyToUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.SetConversionCurrencyFromUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.SetConversionCurrencyToUseCase
import com.ofilip.exchange_rates.ui.component.field.DecimalTextFieldValue
import com.ofilip.exchange_rates.ui.util.ConversionEvent
import com.ofilip.exchange_rates.ui.util.CurrencyConversionProcessor
import com.ofilip.exchange_rates.ui.util.EventType
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.retry
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
    private val uiErrorConverter: UiErrorConverter,
    private val getConversionCurrencyFromUseCase: GetConversionCurrencyFromUseCase,
    private val getConversionCurrencyToUseCase: GetConversionCurrencyToUseCase,
    private val setConversionCurrencyToUseCase: SetConversionCurrencyToUseCase,
    private val setConversionCurrencyFromUseCase: SetConversionCurrencyFromUseCase,
    private val currencyConversionProcessor: CurrencyConversionProcessor
) : ViewModel() {

    private val _uiState = MutableStateFlow(CurrencyConversionUiState())
    val uiState: StateFlow<CurrencyConversionUiState> get() = _uiState

    init {
        loadConversionCurrencies()
        startCurrencyConversionProcessorObserving()
    }

    private fun loadConversionCurrencies() {
        // Observe changes in conversion currencies from and to
        getConversionCurrencyFromUseCase.execute()
            .combine(getConversionCurrencyToUseCase.execute()) { from, to ->
                from to to
            }
            .catch {
                _uiState.value = uiState.value.copy(
                    convertCurrencyFromErrorMessage = uiErrorConverter.convertToText(it),
                    convertCurrencyToErrorMessage = uiErrorConverter.convertToText(it)
                )
            }.collectIn(viewModelScope) { (from, to) ->
                // Determinate which currency was changed
                val eventType = when {
                    from != uiState.value.convertCurrencyFrom -> EventType.CurrencyFromChanged
                    to != uiState.value.convertCurrencyTo -> EventType.CurrencyToChanged
                    else -> null
                }
                // If Any currency was changed
                if (eventType != null) {
                    // Update UI state
                    _uiState.value = uiState.value.copy(
                        convertCurrencyFrom = from,
                        convertCurrencyTo = to
                    )
                    // Trigger conversion
                    currencyConversionProcessor.addEvent(
                        ConversionEvent(
                            type = eventType,
                            amountFrom = uiState.value.convertAmountFrom,
                            amountTo = uiState.value.convertAmountTo,
                            currencyFrom = from,
                            currencyTo = to
                        )
                    )
                }
            }
    }

    private fun startCurrencyConversionProcessorObserving() {
        viewModelScope.launch {
            // Using this processor to prevent from issues caused by fast typing
            // and potential lagging in the UI
            currencyConversionProcessor
                .resultFlow
                .retry()
                .catch {
                    _uiState.value = uiState.value.copy(
                        conversionErrorMessage = uiErrorConverter.convertToText(it)
                    )
                }
                .collectIn(viewModelScope) {
                    _uiState.value = uiState.value.copy(
                        convertAmountFrom = it.amountFrom,
                        convertAmountTo = it.amountTo,
                        convertCurrencyFrom = it.currencyFrom,
                        convertCurrencyTo = it.currencyTo,
                        conversionErrorMessage = null
                    )
                }
        }
    }

    fun onConvertAmountFromChanged(value: DecimalTextFieldValue) {
        _uiState.value = uiState.value.copy(
            convertAmountFrom = value,
            conversionErrorMessage = null
        )
        currencyConversionProcessor.addEvent(
            ConversionEvent(
                type = EventType.AmountFromChanged,
                amountFrom = value,
                currencyFrom = uiState.value.convertCurrencyFrom,
                currencyTo = uiState.value.convertCurrencyTo
            )
        )
    }

    fun onConvertAmountToChanged(value: DecimalTextFieldValue) {
        _uiState.value = uiState.value.copy(
            convertAmountTo = value,
            conversionErrorMessage = null
        )
        currencyConversionProcessor.addEvent(
            ConversionEvent(
                type = EventType.AmountToChanged,
                amountTo = value,
                currencyFrom = uiState.value.convertCurrencyFrom,
                currencyTo = uiState.value.convertCurrencyTo
            )
        )
    }

    fun setConversionCurrencyFrom(currency: String) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _uiState.value = _uiState.value.copy(
                conversionErrorMessage = uiErrorConverter.convertToText(throwable)
            )
        }) {
            // UI state is updated in loadConversionCurrencies
            setConversionCurrencyFromUseCase.execute(currency)
        }
    }

    fun setConversionCurrencyTo(currency: String) {
        viewModelScope.launch(CoroutineExceptionHandler { _, throwable ->
            _uiState.value = _uiState.value.copy(
                conversionErrorMessage = uiErrorConverter.convertToText(throwable)
            )
        }) {
            // UI state is updated in loadConversionCurrencies
            setConversionCurrencyToUseCase.execute(currency)
        }
    }
}


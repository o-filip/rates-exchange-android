package com.ofilip.exchange_rates.ui.util

import com.ofilip.exchange_rates.core.error.UiError
import com.ofilip.exchange_rates.domain.useCase.conversion.ConvertCurrencyUseCase
import com.ofilip.exchange_rates.ui.component.field.DecimalTextFieldValue
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest

/**
 * Processor that handles currency conversion events and produces conversion results
 *
 * When user types the amount faster than the conversion can be calculated, it can cause
 * lagging in the UI. To prevent this, we use a processor that handles the events and
 * makes the conversion and if the calculation is not finished before the next event,
 * the previous calculation is discarded.
 */
interface CurrencyConversionProcessor {
    fun addEvent(event: ConversionEvent)

    val resultFlow: Flow<ConversionResult>
}

class CurrencyConversionProcessorImpl @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
) : CurrencyConversionProcessor {
    private val inputFlow = MutableStateFlow<ConversionEvent?>(null)

    /**
     * Flow that emits the conversion results
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override val resultFlow = inputFlow
        .filterNotNull()
        .mapLatest { event ->
            // Based on the event type, we convert the amount from one currency to another
            when (event.type) {
                EventType.AmountFromChanged,
                EventType.CurrencyFromChanged -> convertAmountFrom(event)

                EventType.AmountToChanged,
                EventType.CurrencyToChanged -> convertAmountTo(event)
            }
        }
        // If the conversion fails, we ignore the event
        .filterNotNull()

    /**
     * Adds a new event to the processor
     */
    override fun addEvent(event: ConversionEvent) {
        inputFlow.value = event
    }

    private suspend fun convertAmountFrom(event: ConversionEvent): ConversionResult? {
        if (event.amountFrom == null) {
            return null
        }

        convertAmount(
            event.amountFrom.number,
            event.currencyFrom,
            event.currencyTo
        ).let { amountTo ->
            return ConversionResult(
                amountFrom = event.amountFrom,
                amountTo = DecimalTextFieldValue.create(amountTo),
                currencyFrom = event.currencyFrom,
                currencyTo = event.currencyTo
            )
        }
    }

    private suspend fun convertAmountTo(event: ConversionEvent): ConversionResult? {
        if (event.amountTo == null) {
            return null
        }

        convertAmount(
            event.amountTo.number,
            event.currencyTo,
            event.currencyFrom
        ).let { amountFrom ->
            return ConversionResult(
                amountFrom = DecimalTextFieldValue.create(amountFrom),
                amountTo = event.amountTo,
                currencyFrom = event.currencyFrom,
                currencyTo = event.currencyTo
            )
        }
    }

    private suspend fun convertAmount(
        amount: Double?,
        fromCurrency: String?,
        toCurrency: String?
    ): Double? =
        if (amount == null) {
            null
        } else if (fromCurrency == null || toCurrency == null) {
            throw UiError.CurrencyNotSelected
        } else {
            convertCurrencyUseCase.execute(
                amount,
                fromCurrency,
                toCurrency
            )
        }
}


data class ConversionEvent(
    val type: EventType,
    val amountFrom: DecimalTextFieldValue? = null,
    val amountTo: DecimalTextFieldValue? = null,
    val currencyFrom: String? = null,
    val currencyTo: String? = null
)

data class ConversionResult(
    val amountFrom: DecimalTextFieldValue,
    val amountTo: DecimalTextFieldValue,
    val currencyFrom: String?,
    val currencyTo: String?,
)

enum class EventType {
    AmountFromChanged,
    AmountToChanged,
    CurrencyFromChanged,
    CurrencyToChanged
}

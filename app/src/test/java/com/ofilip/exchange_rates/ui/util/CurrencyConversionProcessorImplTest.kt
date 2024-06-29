package com.ofilip.exchange_rates.ui.util

import com.ofilip.exchange_rates.core.error.UiError
import com.ofilip.exchange_rates.domain.useCase.conversion.ConvertCurrencyUseCase
import com.ofilip.exchange_rates.ui.component.field.DecimalTextFieldValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class CurrencyConversionProcessorImplTest {
    private val mockConvertCurrencyUseCase: ConvertCurrencyUseCase = mock()
    private lateinit var processor: CurrencyConversionProcessorImpl

    @BeforeEach
    fun setUp() {
        processor = CurrencyConversionProcessorImpl(
            mockConvertCurrencyUseCase, UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `addEvent should process AmountFromChanged event and emit ConversionResult`() =
        runBlocking {
            // Given
            val event = ConversionEvent(
                type = EventType.AmountFromChanged,
                amountFrom = DecimalTextFieldValue.create(100.0),
                currencyFrom = "USD",
                currencyTo = "EUR"
            )
            val expectedResult = ConversionResult(
                amountFrom = DecimalTextFieldValue.create(100.0),
                amountTo = DecimalTextFieldValue.create(85.0),
                currencyFrom = "USD",
                currencyTo = "EUR"
            )

            mockConvertCurrencyUseCase.stub {
                onBlocking { execute(100.0, "USD", "EUR") } doReturn 85.0
            }

            // When
            processor.addEvent(event)
            val result = processor.resultFlow.first()

            // Then
            verify(mockConvertCurrencyUseCase).execute(100.0, "USD", "EUR")
            assertConversionResultEquals(expectedResult, result)
        }

    @Test
    fun `addEvent should process AmountToChanged event and emit ConversionResult`() = runBlocking {
        // Given
        val event = ConversionEvent(
            type = EventType.AmountToChanged,
            amountTo = DecimalTextFieldValue.create(85.0),
            currencyFrom = "USD",
            currencyTo = "EUR"
        )
        val expectedResult = ConversionResult(
            amountFrom = DecimalTextFieldValue.create(100.0),
            amountTo = DecimalTextFieldValue.create(85.0),
            currencyFrom = "USD",
            currencyTo = "EUR"
        )

        mockConvertCurrencyUseCase.stub {
            onBlocking { execute(85.0, "EUR", "USD") } doReturn 100.0
        }

        // When
        processor.addEvent(event)
        val result = processor.resultFlow.first()

        // Then
        verify(mockConvertCurrencyUseCase).execute(85.0, "EUR", "USD")
        assertConversionResultEquals(expectedResult, result)
    }

    @Test
    fun `convertAmount should throw UiError if fromCurrency or toCurrency is null`() = runBlocking {
        // Given
        val event = ConversionEvent(
            type = EventType.AmountFromChanged,
            amountFrom = DecimalTextFieldValue.create(100.0),
            currencyFrom = null,
            currencyTo = "EUR"
        )

        // When
        val exception = assertThrows<UiError.CurrencyNotSelected> {
            runBlocking {
                processor.addEvent(event)
                processor.resultFlow.first()
            }
        }

        // Then
        assertEquals(UiError.CurrencyNotSelected, exception)
    }

    @Test
    fun `addEvent should handle CurrencyFromChanged event`() = runBlocking {
        // Given
        val event = ConversionEvent(
            type = EventType.CurrencyFromChanged,
            amountFrom = DecimalTextFieldValue.create(100.0),
            currencyFrom = "USD",
            currencyTo = "EUR"
        )
        val expectedResult = ConversionResult(
            amountFrom = DecimalTextFieldValue.create(100.0),
            amountTo = DecimalTextFieldValue.create(85.0),
            currencyFrom = "USD",
            currencyTo = "EUR"
        )
        mockConvertCurrencyUseCase.stub {
            onBlocking { execute(100.0, "USD", "EUR") } doReturn 85.0
        }

        // When
        processor.addEvent(event)
        val result = processor.resultFlow.first()

        // Then
        verify(mockConvertCurrencyUseCase).execute(100.0, "USD", "EUR")
        assertConversionResultEquals(expectedResult, result)
    }

    @Test
    fun `addEvent should handle CurrencyToChanged event`() = runBlocking {
        // Given
        val event = ConversionEvent(
            type = EventType.CurrencyToChanged,
            amountTo = DecimalTextFieldValue.create(85.0),
            currencyFrom = "USD",
            currencyTo = "EUR"
        )
        val expectedResult = ConversionResult(
            amountFrom = DecimalTextFieldValue.create(100.0),
            amountTo = DecimalTextFieldValue.create(85.0),
            currencyFrom = "USD",
            currencyTo = "EUR"
        )

        mockConvertCurrencyUseCase.stub {
            onBlocking { execute(85.0, "EUR", "USD") } doReturn 100.0
        }

        // When
        processor.addEvent(event)
        val result = processor.resultFlow.first()

        // Then
        verify(mockConvertCurrencyUseCase).execute(85.0, "EUR", "USD")
        assertConversionResultEquals(expectedResult, result)
    }

    private fun assertConversionResultEquals(expected: ConversionResult, actual: ConversionResult) {
        assertEquals(expected.amountFrom.number, actual.amountFrom.number)
        assertEquals(expected.amountTo.number, actual.amountTo.number)
        assertEquals(expected.currencyFrom, actual.currencyFrom)
        assertEquals(expected.currencyTo, actual.currencyTo)
    }
}

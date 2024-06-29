package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class SetConversionCurrencyToUseCaseImplTest {
    private val mockCurrencyRepository: CurrencyRepository = mock()
    private val useCase = SetConversionCurrencyToUseCaseImpl(
        currencyRepository = mockCurrencyRepository
    )

    @Test
    fun `execute should set conversion currency to in repository`() = runBlocking {
        // Given
        val conversionCurrency = "USD"
        mockCurrencyRepository.stub {
            onBlocking {
                setConversionCurrencyTo(conversionCurrency)
            } doReturn (Unit)
        }

        // When
        useCase.execute(conversionCurrency)

        // Then
        verify(mockCurrencyRepository).setConversionCurrencyTo(conversionCurrency)
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() =
        runBlocking {
            // Given
            val conversionCurrency = "USD"
            val exception = RuntimeException("Failed to set conversion currency to")
            mockCurrencyRepository.stub {
                onBlocking {
                    setConversionCurrencyTo(conversionCurrency)
                }.thenThrow(exception)
            }

            // When
            val thrownException = assertThrows<Exception> {
                useCase.execute(conversionCurrency)
            }

            // Then
            verify(mockCurrencyRepository).setConversionCurrencyTo(conversionCurrency)
            assertEquals(exception, thrownException)
        }
}

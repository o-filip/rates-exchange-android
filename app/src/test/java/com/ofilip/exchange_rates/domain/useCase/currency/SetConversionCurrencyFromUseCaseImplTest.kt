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

class SetConversionCurrencyFromUseCaseImplTest {
    private val mockCurrencyRepository: CurrencyRepository = mock()
    val useCase = SetConversionCurrencyFromUseCaseImpl(
        currencyRepository = mockCurrencyRepository
    )

    @Test
    fun `execute should set conversion currency from in repository`() = runBlocking {
        // Given
        val conversionCurrency = "USD"
        mockCurrencyRepository.stub {
            onBlocking {
                setConversionCurrencyFrom(
                    conversionCurrency
                )
            } doReturn (Unit)
        }

        // When
        val result = useCase.execute(conversionCurrency)

        // Then
        verify(mockCurrencyRepository).setConversionCurrencyFrom(conversionCurrency)
        assertEquals(Unit, result)
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() =
        runBlocking {
            // Given
            val conversionCurrency = "USD"
            val exception = RuntimeException("Failed to set conversion currency from")
            mockCurrencyRepository.stub {
                onBlocking {
                    setConversionCurrencyFrom(
                        conversionCurrency
                    )
                }.thenThrow(exception)
            }

            // When
            val thrownException = assertThrows<RuntimeException> {
                useCase.execute(conversionCurrency)
            }

            // Then
            verify(mockCurrencyRepository).setConversionCurrencyFrom(conversionCurrency)
            assertEquals(exception, thrownException)
        }
}

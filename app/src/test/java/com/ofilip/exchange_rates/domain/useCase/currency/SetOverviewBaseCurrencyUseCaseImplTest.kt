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

class SetOverviewBaseCurrencyUseCaseImplTest {
    private val mockCurrencyRepository: CurrencyRepository = mock()
    private val useCase = SetOverviewBaseCurrencyUseCaseImpl(
        currencyRepository = mockCurrencyRepository
    )

    @Test
    fun `execute should set overview base currency in repository`() = runBlocking {
        // Given
        val baseCurrency = "USD"
        mockCurrencyRepository.stub {
            onBlocking {
                setOverviewBaseCurrency(baseCurrency)
            } doReturn (Unit)
        }

        // When
        val result = useCase.execute(baseCurrency)

        // Then
        verify(mockCurrencyRepository).setOverviewBaseCurrency(baseCurrency)
        assertEquals(Unit, result)
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() =
        runBlocking {
            // Given
            val baseCurrency = "USD"
            val exception = RuntimeException("Failed to set overview base currency")
            mockCurrencyRepository.stub {
                onBlocking {
                    setOverviewBaseCurrency(baseCurrency)
                }.thenThrow(exception)
            }

            // When
            val throwException = assertThrows<Exception> {
                useCase.execute(baseCurrency)
            }

            // Then
            verify(mockCurrencyRepository).setOverviewBaseCurrency(baseCurrency)
            assertEquals(exception, throwException)
        }
}

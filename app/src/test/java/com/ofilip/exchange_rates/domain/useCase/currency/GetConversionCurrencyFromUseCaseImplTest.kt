package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.fixtures.toFlowOfSuccess
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class GetConversionCurrencyFromUseCaseImplTest {
    private val mockCurrencyRepository: CurrencyRepository = mock()

    private val useCase: GetConversionCurrencyFromUseCaseImpl =
        GetConversionCurrencyFromUseCaseImpl(
            currencyRepository = mockCurrencyRepository
        )

    @Test
    fun `execute should return conversion currency from repository`() = runBlocking {
        // Given
        val mockConversionCurrency = "USD"
        mockCurrencyRepository.stub {
            onBlocking { conversionCurrencyFrom }.thenReturn(mockConversionCurrency.toFlowOfSuccess())
        }

        // When
        val result = useCase.execute()

        // Then
        verify(mockCurrencyRepository).conversionCurrencyFrom
        assertEquals(Result.success(mockConversionCurrency), result.last())
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() = runBlocking {
        // Given
        val exception = Exception("Failed to retrieve conversion currency from")
        mockCurrencyRepository.stub {
            onBlocking { conversionCurrencyFrom }.thenReturn(
                flowOf(Result.failure(exception))
            )
        }

        // When
        val result = useCase.execute()

        // Then
        verify(mockCurrencyRepository).conversionCurrencyFrom
        assertEquals(Result.failure<Currency>(exception), result.last())
    }
}

package com.ofilip.exchange_rates.domain.useCase.currency

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

class GetConversionCurrencyToUseCaseImplTest {
    private val mockCurrencyRepository: CurrencyRepository = mock()
    val useCase: GetConversionCurrencyToUseCaseImpl =
        GetConversionCurrencyToUseCaseImpl(mockCurrencyRepository)

    @Test
    fun `execute should return conversion currency to from repository`() = runBlocking {
        // Given
        val mockConversionCurrency = "USD"
        mockCurrencyRepository.stub {
            onBlocking { conversionCurrencyTo }.thenReturn(mockConversionCurrency.toFlowOfSuccess())
        }

        // When
        val result = useCase.execute()

        // Then
        verify(mockCurrencyRepository).conversionCurrencyTo
        assertEquals(Result.success(mockConversionCurrency), result.last())
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() = runBlocking {
        // Given
        val exception = Exception("Failed to retrieve conversion currency to")
        mockCurrencyRepository.stub {
            onBlocking { conversionCurrencyTo }.thenReturn(
                flowOf(Result.failure(exception))
            )
        }

        // When
        val result = useCase.execute()

        // Then
        verify(mockCurrencyRepository).conversionCurrencyTo
        assertEquals(Result.failure<String>(exception), result.last())
    }
}

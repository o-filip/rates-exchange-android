package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.fixtures.Fixtures
import com.ofilip.exchange_rates.fixtures.toFlowOfSuccess
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class GetCurrencyUseCaseImplTest {
    private val mockCurrencyRepository: CurrencyRepository = mock()

    val useCase = GetCurrencyUseCaseImpl(
        currencyRepository = mockCurrencyRepository
    )

    @Test
    fun `execute should return currency from repository`() = runBlocking {
        // Given
        val currency = Fixtures.currencies.first()

        mockCurrencyRepository.stub {
            onBlocking { getCurrency(any()) } doReturn (currency.toFlowOfSuccess())
        }

        // When
        val result = useCase.execute(currencyCode = currency.currencyCode)

        // Then
        verify(mockCurrencyRepository).getCurrency(currency.currencyCode)
        assertEquals(Result.success(currency), result.last())
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() =
        runBlocking {
            // Given
            val currency = Fixtures.currencies.first()
            val exception = Exception("Failed to retrieve currency")

            mockCurrencyRepository.stub {
                onBlocking { getCurrency(any()) } doReturn (flowOf(Result.failure(exception)))
            }

            // When
            val result = useCase.execute(currencyCode = currency.currencyCode)

            // Then
            verify(mockCurrencyRepository).getCurrency(currency.currencyCode)
            assertEquals(Result.failure<Currency>(exception), result.last())
        }
}

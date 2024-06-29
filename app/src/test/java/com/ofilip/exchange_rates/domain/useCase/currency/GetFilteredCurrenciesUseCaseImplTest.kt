package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.fixtures.Fixtures
import com.ofilip.exchange_rates.fixtures.toFlowOfSuccess
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify


class GetFilteredCurrenciesUseCaseImplTest {

    private val mockCurrencyRepository: CurrencyRepository = mock()
    private val useCase = GetFilteredCurrenciesUseCaseImpl(
        mockCurrencyRepository
    )

    @Test
    fun `execute should return retrieve filtered currencies from repository`() =
        runBlocking {
            // Given
            val query = "USD"
            val onlyFavorites = true
            val currencies = Fixtures.currencies

            mockCurrencyRepository.stub {
                onBlocking {
                    getCurrencies(any(), any())
                }.thenReturn(currencies.toFlowOfSuccess())
            }

            // When
            val result = useCase.execute(query, onlyFavorites).toList()

            // Then
            verify(mockCurrencyRepository).getCurrencies(query, onlyFavorites)
            assertEquals(Result.success(currencies), result.last())
        }

    @Test
    fun `execute should return failure when currency repository returns error result`() =
        runBlocking {
            // Given
            val query = "EUR"
            val onlyFavorites = false
            val exception = Exception("Failed to retrieve currencies")
            mockCurrencyRepository.stub {
                onBlocking { getCurrencies(any(), any()) }.thenReturn(
                    flowOf(Result.failure(exception))
                )
            }

            // When
            val result = useCase.execute(query, onlyFavorites).first()

            // Then
            verify(mockCurrencyRepository).getCurrencies(query, onlyFavorites)
            assertEquals(Result.failure<List<Currency>>(exception), result)
        }
}

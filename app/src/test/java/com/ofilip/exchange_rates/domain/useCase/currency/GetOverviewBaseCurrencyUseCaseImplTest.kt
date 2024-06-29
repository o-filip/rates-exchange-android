package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.fixtures.toFlowOfSuccess
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class GetOverviewBaseCurrencyUseCaseImplTest {
    private val mockCurrencyRepository: CurrencyRepository = mock()
    private val useCase: GetOverviewBaseCurrencyUseCaseImpl =
        GetOverviewBaseCurrencyUseCaseImpl(mockCurrencyRepository)

    @Test
    fun `execute should return overview base currency from repository`() = runBlocking {
        // Given
        val mockOverviewBaseCurrency = "USD"
        mockCurrencyRepository.stub {
            onBlocking { overviewBaseCurrency } doReturn (mockOverviewBaseCurrency.toFlowOfSuccess())
        }

        // When
        val result = useCase.execute()

        // Then
        verify(mockCurrencyRepository).overviewBaseCurrency
        assertEquals(Result.success(mockOverviewBaseCurrency), result.last())
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() =
        runBlocking {
            // Given
            val exception = Exception("Failed to retrieve overview base currency")
            mockCurrencyRepository.stub {
                onBlocking { overviewBaseCurrency } doReturn (
                        flowOf(Result.failure(exception))
                        )
            }

            // When
            val result = useCase.execute()

            // Then
            verify(mockCurrencyRepository).overviewBaseCurrency
            assertEquals(Result.failure<String>(exception), result.last())
        }
}

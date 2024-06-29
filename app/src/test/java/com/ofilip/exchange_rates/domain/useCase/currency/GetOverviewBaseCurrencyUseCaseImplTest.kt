package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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
            onBlocking { overviewBaseCurrency } doReturn (flowOf(mockOverviewBaseCurrency))
        }

        // When
        val result = useCase.execute()

        // Then
        verify(mockCurrencyRepository).overviewBaseCurrency
        assertEquals(mockOverviewBaseCurrency, result.last())
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() =
        runBlocking {
            // Given
            val exception = RuntimeException("Failed to retrieve overview base currency")
            mockCurrencyRepository.stub {
                onBlocking { overviewBaseCurrency }.thenThrow(exception)
            }

            // When
            val thrownException = assertThrows<RuntimeException> {
                useCase.execute()
            }

            // Then
            verify(mockCurrencyRepository).overviewBaseCurrency
            assertEquals(exception, thrownException)
        }
}

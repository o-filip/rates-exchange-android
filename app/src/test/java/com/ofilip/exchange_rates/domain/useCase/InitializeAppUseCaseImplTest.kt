package com.ofilip.exchange_rates.domain.useCase

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class InitializeAppUseCaseImplTest {

    private val mockCurrencyRepository: CurrencyRepository = mock()
    private val useCase = InitializeAppUseCaseImpl(mockCurrencyRepository)

    @Test
    fun `execute should return success when currencies are already loaded`() = runBlocking {
        // Given
        mockCurrencyRepository.stub {
            onBlocking { areCurrenciesLoaded() } doReturn true
        }

        // When
        val result = useCase.execute()

        // Then
        verify(mockCurrencyRepository).areCurrenciesLoaded()
        verify(mockCurrencyRepository, never()).prefetchCurrencies()
        assertEquals(Unit, result)
    }

    @Test
    fun `execute should return success and prefetch currencies when currencies are not loaded`() =
        runBlocking {
            // Given
            mockCurrencyRepository.stub {
                onBlocking { areCurrenciesLoaded() } doReturn false
                onBlocking { prefetchCurrencies() } doReturn Unit
            }

            // When
            val result = useCase.execute()

            // Then
            verify(mockCurrencyRepository).areCurrenciesLoaded()
            verify(mockCurrencyRepository).prefetchCurrencies()
            assertEquals(Unit, result)
        }

    @Test
    fun `execute should return failure when an exception occurs`() = runBlocking {
        // Given
        val mockException = RuntimeException("Failed to load currencies")

        mockCurrencyRepository.stub {
            onBlocking { areCurrenciesLoaded() }.thenThrow(mockException)
        }

        // When
        val thrownException = assertThrows<RuntimeException> {
            useCase.execute()
        }

        // Then
        verify(mockCurrencyRepository).areCurrenciesLoaded()
        verify(mockCurrencyRepository, never()).prefetchCurrencies()
        assertEquals(mockException, thrownException)
    }
}

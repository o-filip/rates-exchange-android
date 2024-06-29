package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.fixtures.Fixtures
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class UpdateCurrencyFavoriteStateUseCaseImplTest {
    private val mockCurrencyRepository: CurrencyRepository = mock()

    val useCase = UpdateCurrencyFavoriteStateUseCaseImpl(
        currencyRepository = mockCurrencyRepository
    )

    @Test
    fun `execute should update currency favorite state in repository`() = runBlocking {
        // Given
        val currency = Fixtures.currencies.first()
        val isFavorite = true

        mockCurrencyRepository.stub {
            onBlocking {
                updateCurrencyFavoriteState(
                    currency,
                    isFavorite
                )
            } doReturn (Unit)
        }

        // When
        useCase.execute(currency, isFavorite)

        // Then
        verify(mockCurrencyRepository).updateCurrencyFavoriteState(currency, isFavorite)
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() =
        runBlocking {
            // Given
            val currency = Fixtures.currencies.first()
            val isFavorite = true
            val exception = RuntimeException("Failed to update currency favorite state")

            mockCurrencyRepository.stub {
                onBlocking {
                    updateCurrencyFavoriteState(
                        currency,
                        isFavorite
                    )
                }.thenThrow(exception)
            }

            // When
            val thrownException = assertThrows<RuntimeException> {
                useCase.execute(currency, isFavorite)
            }

            // Then
            verify(mockCurrencyRepository).updateCurrencyFavoriteState(currency, isFavorite)
            assertEquals(exception, thrownException)
        }
}

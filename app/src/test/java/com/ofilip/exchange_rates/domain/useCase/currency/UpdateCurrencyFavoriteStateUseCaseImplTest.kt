package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.fixtures.Fixtures
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
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
            } doReturn (Result.success(Unit))
        }

        // When
        val result = useCase.execute(currency, isFavorite)

        // Then
        verify(mockCurrencyRepository).updateCurrencyFavoriteState(currency, isFavorite)
        assert(result.isSuccess)
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() = runBlocking {
        // Given
        val currency = Fixtures.currencies.first()
        val isFavorite = true
        val exception = Exception("Failed to update currency favorite state")

        mockCurrencyRepository.stub {
            onBlocking {
                updateCurrencyFavoriteState(
                    currency,
                    isFavorite
                )
            } doReturn (Result.failure(exception))
        }

        // When
        val result = useCase.execute(currency, isFavorite)

        // Then
        verify(mockCurrencyRepository).updateCurrencyFavoriteState(currency, isFavorite)
        assert(result.isFailure)

    }
}

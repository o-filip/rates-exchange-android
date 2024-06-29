package com.ofilip.exchange_rates.domain.useCase.currency

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

class SetConversionCurrencyToUseCaseImplTest {
    private val mockCurrencyRepository: CurrencyRepository = mock()
    private val useCase = SetConversionCurrencyToUseCaseImpl(
        currencyRepository = mockCurrencyRepository
    )

    @Test
    fun `execute should set conversion currency to in repository`() = runBlocking {
        // Given
        val conversionCurrency = "USD"
        mockCurrencyRepository.stub {
            onBlocking {
                setConversionCurrencyTo(conversionCurrency)
            } doReturn (Result.success(Unit))
        }

        // When
        val result = useCase.execute(conversionCurrency)

        // Then
        verify(mockCurrencyRepository).setConversionCurrencyTo(conversionCurrency)
        assert(result.isSuccess)
    }

    @Test
    fun `execute should return failure when currency repository returns error result`() = runBlocking {
        // Given
        val conversionCurrency = "USD"
        val exception = Exception("Failed to set conversion currency to")
        mockCurrencyRepository.stub {
            onBlocking {
                setConversionCurrencyTo(conversionCurrency)
            } doReturn (Result.failure(exception))
        }

        // When
        val result = useCase.execute(conversionCurrency)

        // Then
        verify(mockCurrencyRepository).setConversionCurrencyTo(conversionCurrency)
        assert(result.isFailure)
    }
}

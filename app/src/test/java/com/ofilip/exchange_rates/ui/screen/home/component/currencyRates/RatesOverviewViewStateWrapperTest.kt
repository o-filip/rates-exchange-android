package com.ofilip.exchange_rates.ui.screen.home.component.currencyRates

import com.ofilip.exchange_rates.domain.useCase.currency.GetOverviewBaseCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.SetOverviewBaseCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.rate.GetRatesForOverviewUseCase
import com.ofilip.exchange_rates.fixtures.Fixtures
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class RatesOverviewViewStateWrapperTest {

    private val mockGetOverviewRatesUseCase: GetRatesForOverviewUseCase = mock()
    private val mockGetOverviewBaseCurrencyUseCase: GetOverviewBaseCurrencyUseCase = mock()
    private val mockSetOverviewBaseCurrencyUseCase: SetOverviewBaseCurrencyUseCase = mock()
    private val mockUiErrorConverter: UiErrorConverter = mock()
    private val testCoroutineScope: TestDispatcher = StandardTestDispatcher()

    private fun createViewModel() = CurrencyRatesViewModel(
        getOverviewRatesUseCase = mockGetOverviewRatesUseCase,
        getOverviewBaseCurrencyUseCase = mockGetOverviewBaseCurrencyUseCase,
        setOverviewBaseCurrencyUseCase = mockSetOverviewBaseCurrencyUseCase,
        uiErrorConverter = mockUiErrorConverter
    )

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testCoroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads rates and sets overview currency`() = runTest {
        // Given
        val mockRates = Fixtures.localRates
        val mockOverviewCurrency = "USD"
        val expectedState = CurrencyRatesUiState(
            rates = mockRates,
            ratesLoading = false,
            overviewCurrency = mockOverviewCurrency,
            ratesLoadErrorMessage = null
        )

        mockGetOverviewBaseCurrencyUseCase.stub {
            onBlocking { execute() } doReturn flowOf(mockOverviewCurrency)
        }
        mockGetOverviewRatesUseCase.stub {
            onBlocking { execute() } doReturn flowOf(mockRates)
        }

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun `init sets error message when loading of base currency fails`() = runTest {
        // Given
        val errorMessage = "Failed to load rates"
        val exception = RuntimeException(errorMessage)
        val mockRates = Fixtures.localRates

        mockGetOverviewBaseCurrencyUseCase.stub {
            onBlocking { execute() } doReturn flow { throw exception }
        }
        mockUiErrorConverter.stub {
            on { convertToText(exception) } doReturn errorMessage
        }
        mockGetOverviewRatesUseCase.stub {
            onBlocking { execute() } doReturn flowOf(mockRates)
        }

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        verify(mockUiErrorConverter).convertToText(exception)
        verify(mockGetOverviewRatesUseCase).execute()
        verify(mockGetOverviewBaseCurrencyUseCase).execute()
        assertEquals(errorMessage, viewModel.uiState.value.baseCurrencyErrorMessage)
        assertEquals(mockRates, viewModel.uiState.value.rates)
    }

    @Test
    fun `init sets error message when loading of rates fails`() = runTest {
        // Given
        val errorMessage = "Failed to load rates"
        val exception = RuntimeException(errorMessage)
        val mockBaseCurrency = "USD"

        mockGetOverviewBaseCurrencyUseCase.stub {
            onBlocking { execute() } doReturn flowOf(mockBaseCurrency)
        }
        mockUiErrorConverter.stub {
            on { convertToText(exception) } doReturn errorMessage
        }
        mockGetOverviewRatesUseCase.stub {
            onBlocking { execute() } doReturn flow { throw exception }
        }

        // When
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Then
        verify(mockUiErrorConverter).convertToText(exception)
        verify(mockGetOverviewRatesUseCase).execute()
        verify(mockGetOverviewBaseCurrencyUseCase).execute()
        assertEquals(errorMessage, viewModel.uiState.value.ratesLoadErrorMessage)
        assertEquals(mockBaseCurrency, viewModel.uiState.value.overviewCurrency)
    }

    @Test
    fun `refreshData reloads rates`() = runTest {
        // Given
        val mockRates = Fixtures.localRates
        val mockOverviewCurrency = "USD"

        mockGetOverviewBaseCurrencyUseCase.stub {
            onBlocking { execute() } doReturn flowOf(mockOverviewCurrency)
        }
        mockGetOverviewRatesUseCase.stub {
            onBlocking { execute() } doReturn flowOf(mockRates)
        }

        // When
        val viewModel = createViewModel()
        viewModel.refreshData()
        advanceUntilIdle()

        // Then
        verify(mockGetOverviewRatesUseCase, times(2)).execute()
    }
}

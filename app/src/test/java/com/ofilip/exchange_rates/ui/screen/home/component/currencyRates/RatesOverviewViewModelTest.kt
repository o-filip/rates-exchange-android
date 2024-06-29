package com.ofilip.exchange_rates.ui.screen.home.component.currencyRates

import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.domain.useCase.currency.GetOverviewBaseCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.SetOverviewBaseCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.rate.GetRatesForOverviewUseCase
import com.ofilip.exchange_rates.fixtures.Fixtures
import com.ofilip.exchange_rates.fixtures.toFlowOfFailure
import com.ofilip.exchange_rates.fixtures.toFlowOfSuccess
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class RatesOverviewViewModelTest {
    private lateinit var viewModel: CurrencyRatesViewModel

    private val mockGetOverviewRatesUseCase: GetRatesForOverviewUseCase = mock()
    private val mockGetOverviewBaseCurrencyUseCase: GetOverviewBaseCurrencyUseCase = mock()
    private val mockSetOverviewBaseCurrencyUseCase: SetOverviewBaseCurrencyUseCase = mock()
    private val mockUiErrorConverter: UiErrorConverter = mock()
    private val testCoroutineScope: TestDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testCoroutineScope)

        viewModel = CurrencyRatesViewModel(
            getOverviewRatesUseCase = mockGetOverviewRatesUseCase,
            getOverviewBaseCurrencyUseCase = mockGetOverviewBaseCurrencyUseCase,
            setOverviewBaseCurrencyUseCase = mockSetOverviewBaseCurrencyUseCase,
            uiErrorConverter = mockUiErrorConverter
        )
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
            onBlocking { execute() } doReturn mockOverviewCurrency.toFlowOfSuccess()
        }
        mockGetOverviewRatesUseCase.stub {
            onBlocking { execute() } doReturn mockRates.toFlowOfSuccess()
        }

        // When
        viewModel.init()
        advanceUntilIdle()

        // Then
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun `init sets error message when loading of base currency fails`() = runTest {
        // Given
        val errorMessage = "Failed to load rates"
        val exception = Exception(errorMessage)
        val mockRates = Fixtures.localRates

        mockGetOverviewBaseCurrencyUseCase.stub {
            onBlocking { execute() } doReturn exception.toFlowOfFailure()
        }
        mockUiErrorConverter.stub {
            on { convertToText(exception) } doReturn errorMessage
        }
        mockGetOverviewRatesUseCase.stub {
            onBlocking { execute() } doReturn mockRates.toFlowOfSuccess()
        }

        // When
        viewModel.init()
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
        val exception = Exception(errorMessage)
        val mockBaseCurrency = "USD"

        mockGetOverviewBaseCurrencyUseCase.stub {
            onBlocking { execute() } doReturn mockBaseCurrency.toFlowOfSuccess()
        }
        mockUiErrorConverter.stub {
            on { convertToText(exception) } doReturn errorMessage
        }
        mockGetOverviewRatesUseCase.stub {
            onBlocking { execute() } doReturn exception.toFlowOfFailure()
        }

        // When
        viewModel.init()
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
            onBlocking { execute() } doReturn mockOverviewCurrency.toFlowOfSuccess()
        }
        mockGetOverviewRatesUseCase.stub {
            onBlocking { execute() } doReturn mockRates.toFlowOfSuccess()
        }

        // When
        viewModel.refreshData()
        advanceUntilIdle()

        // Then
        verify(mockGetOverviewRatesUseCase).execute()
    }
}

package com.ofilip.exchange_rates.ui.screen.currencySelection

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.domain.useCase.GetFilteredCurrenciesUseCase
import com.ofilip.exchange_rates.fixtures.Fixtures
import com.ofilip.exchange_rates.fixtures.toFlowOfSuccess
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class CurrencySelectionViewModelTest {
    private val mockGetFilteredCurrenciesUseCase: GetFilteredCurrenciesUseCase = mock()
    private val mockCurrencyRepository: CurrencyRepository = mock()
    private val mockUiErrorConverter: UiErrorConverter = mock()
    private val testCoroutineScope: TestDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testCoroutineScope)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createTestSubject(mode: CurrencySelectionMode): CurrencySelectionViewModel =
        CurrencySelectionViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "mode" to mode,
                )
            ),
            getFilteredCurrencies = mockGetFilteredCurrenciesUseCase,
            currencyRepository = mockCurrencyRepository,
            uiErrorConverter = mockUiErrorConverter
        )

    private fun prepareSuccessfulMock(
        mockCurrencies: List<Currency> = Fixtures.currencies,
        preselectedCurrency: String = "USD"
    ) {
        mockCurrencyRepository.stub {
            on { overviewBaseCurrency } doReturn MutableStateFlow(
                Result.success(preselectedCurrency)
            )
        }
        mockGetFilteredCurrenciesUseCase.stub {
            onBlocking { execute(any(), any()) } doReturn mockCurrencies.toFlowOfSuccess()
        }
    }


    @Test
    fun `init initializes preselected currencies and initializes filtering currencies by query text`() =
        runTest {
            // Given
            val viewModel = createTestSubject(CurrencySelectionMode.OverviewCurrency)
            val mockCurrencies = Fixtures.currencies
            val preselectedCurrency = "USD"
            prepareSuccessfulMock(mockCurrencies, preselectedCurrency)

            val expectedState = CurrencySelectionUiState(
                currencies = mockCurrencies,
                query = TextFieldValue(),
                showOnlyFavorites = false,
                selectedCurrency = preselectedCurrency,
                filteringErrorMessage = null,
                userSelectedCurrency = false
            )

            // When
            viewModel.init()
            advanceUntilIdle()

            // Then
            verify(mockCurrencyRepository).overviewBaseCurrency
            verify(mockGetFilteredCurrenciesUseCase).execute("", false)
            assertEquals(expectedState, viewModel.uiState.value)
        }

    @Test
    fun `init prefetches correct currency based on mode for ConversionCurrencyFrom`() =
        runTest {
            // Given
            val viewModel = createTestSubject(CurrencySelectionMode.ConversionCurrencyFrom)
            val mockCurrencies = Fixtures.currencies
            val preselectedCurrency = "USD"
            mockCurrencyRepository.stub {
                on { conversionCurrencyFrom } doReturn MutableStateFlow(
                    Result.success(preselectedCurrency)
                )
            }
            mockGetFilteredCurrenciesUseCase.stub {
                onBlocking { execute(any(), any()) } doReturn mockCurrencies.toFlowOfSuccess()
            }

            // When
            viewModel.init()
            advanceUntilIdle()

            // Then
            verify(mockCurrencyRepository).conversionCurrencyFrom
            assertEquals(preselectedCurrency, viewModel.uiState.value.selectedCurrency)
        }

    @Test
    fun `onQueryUpdated updates UI state with filtered currencies`() = runTest {
        // Given
        val viewModel = createTestSubject(CurrencySelectionMode.OverviewCurrency)
        val preselectedCurrency = "USD"
        val mockCurrencies = Fixtures.currencies
        val queriedCurrency = "EUR"
        prepareSuccessfulMock(mockCurrencies, preselectedCurrency)

        val expectedState = CurrencySelectionUiState(
            currencies = mockCurrencies,
            query = TextFieldValue(queriedCurrency),
            showOnlyFavorites = false,
            selectedCurrency = preselectedCurrency,
            filteringErrorMessage = null,
            userSelectedCurrency = false
        )

        // When
        viewModel.init()
        viewModel.onQueryUpdated(TextFieldValue(queriedCurrency))
        advanceUntilIdle()

        // Then
        verify(mockGetFilteredCurrenciesUseCase).execute(queriedCurrency, false)
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun `toggleShowOnlyFavorites updates UI state with favorite currencies`() = runTest {
        // Given
        val viewModel = createTestSubject(CurrencySelectionMode.OverviewCurrency)
        val preselectedCurrency = "USD"
        val mockCurrencies = Fixtures.currencies
        prepareSuccessfulMock(mockCurrencies, preselectedCurrency)

        val expectedState = CurrencySelectionUiState(
            currencies = mockCurrencies,
            query = TextFieldValue(),
            showOnlyFavorites = true,
            selectedCurrency = preselectedCurrency,
            filteringErrorMessage = null,
            userSelectedCurrency = false
        )

        // When
        viewModel.init()
        viewModel.toggleShowOnlyFavorites()
        advanceUntilIdle()

        // Then
        verify(mockCurrencyRepository).overviewBaseCurrency
        verify(mockGetFilteredCurrenciesUseCase).execute("", true)
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun `toggleCurrencyLike changes currency like status`() = runTest {
        val viewModel = createTestSubject(CurrencySelectionMode.OverviewCurrency)
        val preselectedCurrency = "USD"
        val mockCurrencies = Fixtures.currencies
        prepareSuccessfulMock(mockCurrencies, preselectedCurrency)

        mockCurrencyRepository.stub {
            onBlocking { updateCurrencyFavoriteState(any(), any()) } doReturn Result.success(Unit)
        }

        viewModel.init()
        viewModel.toggleCurrencyLike(mockCurrencies[0])
        advanceUntilIdle()

        verify(mockCurrencyRepository).updateCurrencyFavoriteState(mockCurrencies[0], true)
    }
}

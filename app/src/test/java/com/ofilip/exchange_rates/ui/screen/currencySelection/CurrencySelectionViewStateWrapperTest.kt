package com.ofilip.exchange_rates.ui.screen.currencySelection

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.domain.useCase.currency.GetFilteredCurrenciesUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.UpdateCurrencyFavoriteStateUseCase
import com.ofilip.exchange_rates.fixtures.Fixtures
import com.ofilip.exchange_rates.fixtures.toFlowOfSuccess
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class CurrencySelectionViewStateWrapperTest {
    private val mockGetFilteredCurrenciesUseCase: GetFilteredCurrenciesUseCase = mock()
    private val mockUpdateCurrencyFavoriteStateUseCase: UpdateCurrencyFavoriteStateUseCase = mock()
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

    private fun createTestSubject(
        preselectedCurrencies: String = "",
    ): CurrencySelectionViewModel =
        CurrencySelectionViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "preselectedCurrencies" to preselectedCurrencies,
                )
            ),
            getFilteredCurrencies = mockGetFilteredCurrenciesUseCase,
            updateCurrencyFavoriteStateUseCase = mockUpdateCurrencyFavoriteStateUseCase,
            uiErrorConverter = mockUiErrorConverter
        )

    private fun prepareSuccessfulMock(
        mockCurrencies: List<Currency> = Fixtures.currencies,
    ) {
        mockGetFilteredCurrenciesUseCase.stub {
            onBlocking { execute(any(), any()) } doReturn flowOf(mockCurrencies)
        }
    }


    @Test
    fun `init initializes preselected currencies and initializes filtering currencies by empty query text`() =
        runTest {
            // Given

            val mockCurrencies = Fixtures.currencies
            prepareSuccessfulMock(mockCurrencies)
            val viewModel = createTestSubject(preselectedCurrencies = "EUR")

            val expectedState = CurrencySelectionUiState(
                currencies = mockCurrencies,
                query = TextFieldValue(),
                showOnlyFavorites = false,
                filteringErrorMessage = null,
                userSelectedCurrency = null,
                selectedCurrencies = listOf("EUR")
            )

            // When
            advanceUntilIdle()

            // Then
            verify(mockGetFilteredCurrenciesUseCase).execute("", false)
            assertEquals(expectedState, viewModel.uiState.value)
        }


    @Test
    fun `onQueryUpdated updates UI state with filtered text and calls currency filter use case`() = runTest {
        // Given
        val preselectedCurrency = "USD"
        val mockCurrencies = Fixtures.currencies
        val queriedCurrency = "EUR"
        prepareSuccessfulMock(mockCurrencies)
        val viewModel = createTestSubject(preselectedCurrency)

        mockGetFilteredCurrenciesUseCase.stub {
            onBlocking { execute(any(), any()) } doReturn flowOf(mockCurrencies.subList(0, 1))
        }

        val expectedState = CurrencySelectionUiState(
            currencies = mockCurrencies.subList(0, 1),
            query = TextFieldValue(queriedCurrency),
            showOnlyFavorites = false,
            filteringErrorMessage = null,
            userSelectedCurrency = null,
            selectedCurrencies = listOf(preselectedCurrency)
        )

        // When
        viewModel.onQueryUpdated(TextFieldValue(queriedCurrency))
        advanceUntilIdle()

        // Then
        verify(mockGetFilteredCurrenciesUseCase).execute(queriedCurrency, false)
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun `toggleShowOnlyFavorites updates UI state with favorite toggle and filter currencies`() = runTest {
        // Given
        val mockCurrencies = Fixtures.currencies
        prepareSuccessfulMock(mockCurrencies)
        val viewModel = createTestSubject("USD")

        val expectedState = CurrencySelectionUiState(
            currencies = mockCurrencies,
            query = TextFieldValue(),
            showOnlyFavorites = true,
            filteringErrorMessage = null,
            userSelectedCurrency = null,
            selectedCurrencies = listOf("USD")
        )

        // When
        viewModel.toggleShowOnlyFavorites()
        advanceUntilIdle()

        // Then
        verify(mockGetFilteredCurrenciesUseCase).execute("", true)
        assertEquals(expectedState, viewModel.uiState.value)
    }
}

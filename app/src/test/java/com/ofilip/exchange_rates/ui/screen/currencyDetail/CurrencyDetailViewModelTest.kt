package com.ofilip.exchange_rates.ui.screen.currencyDetail

import androidx.lifecycle.SavedStateHandle
import com.ofilip.exchange_rates.domain.useCase.GetCurrencyUseCase
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

@ExperimentalCoroutinesApi
class CurrencyDetailViewModelTest {
    private lateinit var viewModel: CurrencyDetailViewModel

    private val mockCurrencyCode = "USD"
    private val mockUiErrorConverter: UiErrorConverter = mock()
    private val getCurrencyUseCase: GetCurrencyUseCase = mock()
    private val mockTestCoroutineScope: TestDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(mockTestCoroutineScope)

        viewModel = CurrencyDetailViewModel(
            savedStateHandle = SavedStateHandle(
                mapOf(
                    "currencyCode" to mockCurrencyCode,
                )
            ),
            uiErrorConverter = mockUiErrorConverter,
            getCurrencyUseCase = getCurrencyUseCase,
        )
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init sets currency in UI state`() = runTest {
        // Given
        val mockCurrency = Fixtures.currencies.first()
        getCurrencyUseCase.stub {
            onBlocking { execute(mockCurrencyCode) } doReturn mockCurrency.toFlowOfSuccess()
        }

        val expectedState = CurrencyDetailUiState(
            currency = mockCurrency,
            errorMessage = null
        )

        // When
        viewModel.init()
        advanceUntilIdle()

        // Then
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun `init sets error message in UI state when getting currency fails`() = runTest {
        // Given
        val errorMessage = "Failed to get currency"
        val exception = Exception(errorMessage)
        getCurrencyUseCase.stub {
            onBlocking { execute(mockCurrencyCode) } doReturn exception.toFlowOfFailure()
        }
        mockUiErrorConverter.stub {
            on { convertToText(exception) } doReturn "Failed to get currency"
        }

        val expectedState = CurrencyDetailUiState(
            currency = null,
            errorMessage = errorMessage
        )

        // When
        viewModel.init()
        advanceUntilIdle()

        // Then
        assertEquals(expectedState, viewModel.uiState.value)
    }
}
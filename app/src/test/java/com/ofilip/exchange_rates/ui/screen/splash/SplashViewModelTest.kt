package com.ofilip.exchange_rates.ui.screen.splash

import com.ofilip.exchange_rates.domain.useCase.InitializeAppUseCase
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
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify


@ExperimentalCoroutinesApi
class SplashViewModelTest {
    private val mockInitializeAppUseCase: InitializeAppUseCase = mock()
    private val mockUiErrorConverter: UiErrorConverter = mock()
    private val testCoroutineScope: TestDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: SplashViewModel

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testCoroutineScope)
        viewModel =
            SplashViewModel(mockInitializeAppUseCase, mockUiErrorConverter)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initApp succeeds when initialization use case returns success`() = runTest {
        // Given
        val successResult = Result.success(Unit)
        mockInitializeAppUseCase.stub {
            onBlocking { execute() } doReturn successResult
        }
        val expectedState = SplashUiState(
            isLoading = false,
            errorMessage = null,
            initializedSuccessfully = true
        )

        // When
        viewModel.initApp()
        advanceUntilIdle()

        // Then
        verify(mockInitializeAppUseCase).execute()
        assertEquals(expectedState, viewModel.uiState.value)
    }

    @Test
    fun `initApp fails when initialization use case returns failure`() = runTest {
        // Given
        val errorMessage = "Error message"
        val exception = Exception(errorMessage)
        mockInitializeAppUseCase.stub {
            onBlocking { execute() } doReturn Result.failure(exception)
        }
        mockUiErrorConverter.stub {
            on { convertToText(any(), anyOrNull()) } doReturn errorMessage
        }

        val expectedState = SplashUiState(
            isLoading = false,
            errorMessage = errorMessage,
            initializedSuccessfully = false
        )

        // When
        viewModel.initApp()
        advanceUntilIdle()

        // Then
        val uiState = viewModel.uiState.value

        verify(mockInitializeAppUseCase).execute()
        verify(mockUiErrorConverter).convertToText(eq(exception), any())
        assertEquals(expectedState, uiState)
    }

    @Test
    fun `retry will call initialization use case again`() = runTest {
        val successResult = Result.success(Unit)
        mockInitializeAppUseCase.stub {
            onBlocking { execute() } doReturn successResult
        }

        // When
        viewModel.retry()
        advanceUntilIdle()

        // Then
        verify(mockInitializeAppUseCase).execute()
    }
}

package com.ofilip.exchange_rates.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.R
import com.ofilip.exchange_rates.core.error.DataError
import com.ofilip.exchange_rates.domain.useCase.InitializeAppUseCase
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SplashUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val initializedSuccessfully: Boolean = false
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val initializeAppUseCase: InitializeAppUseCase,
    private val uiErrorConverter: UiErrorConverter
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> get() = _uiState

    fun initApp() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.value = uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

            initializeAppUseCase.execute()
                .onSuccess {
                    _uiState.value = uiState.value.copy(
                        isLoading = false,
                        initializedSuccessfully = true,

                        )
                }
                .onFailure {
                    _uiState.value = uiState.value.copy(
                        isLoading = false,
                        errorMessage = uiErrorConverter.convertToText(
                            it,
                            overrideErrors = { error ->
                                if (error is DataError.Unauthorized) {
                                    R.string.error_data_unauthorized_full_explanation
                                } else {
                                    null
                                }
                            })
                    )
                }
        }
    }

    fun retry() {
        initApp()
    }
}

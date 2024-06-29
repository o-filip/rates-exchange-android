package com.ofilip.exchange_rates.ui.screen.currencySelection

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.core.extensions.collectIn
import com.ofilip.exchange_rates.core.extensions.filterWithPrev
import com.ofilip.exchange_rates.domain.useCase.currency.GetFilteredCurrenciesUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.UpdateCurrencyFavoriteStateUseCase
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch

data class CurrencySelectionUiState(
    val currencies: List<Currency> = emptyList(),
    val query: TextFieldValue = TextFieldValue(),
    val showOnlyFavorites: Boolean = false,
    val selectedCurrency: String? = null,
    val filteringErrorMessage: String? = null,
    val userSelectedCurrency: String? = null,
)

@HiltViewModel
class CurrencySelectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFilteredCurrencies: GetFilteredCurrenciesUseCase,
    private val updateCurrencyFavoriteStateUseCase: UpdateCurrencyFavoriteStateUseCase,
    private val uiErrorConverter: UiErrorConverter
) : ViewModel() {

    private val preselectedCurrency: String =
        checkNotNull(savedStateHandle["preselectedCurrency"]) as String

    private val _uiState = MutableStateFlow(CurrencySelectionUiState())
    val uiState: StateFlow<CurrencySelectionUiState> get() = _uiState

    init {
        _uiState.value = _uiState.value.copy(
            selectedCurrency = preselectedCurrency,
        )
        initFilteringByQuery()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initFilteringByQuery() {
        uiState
            .filterWithPrev { prev, current ->
                // Ignore changes in fields other than text and favorites
                prev?.query?.text != current.query.text || prev.showOnlyFavorites != current.showOnlyFavorites
            }
            .flatMapLatest { uiState ->
                // Load filtered currencies
                getFilteredCurrencies.execute(uiState.query.text, uiState.showOnlyFavorites)
            }
            .retry()
            .catch {
                _uiState.value = uiState.value.copy(
                    filteringErrorMessage = uiErrorConverter.convertToText(it)
                )
            }.collectIn(viewModelScope) { currencies ->
                _uiState.value = uiState.value.copy(
                    currencies = currencies, filteringErrorMessage = null
                )
            }
    }

    fun onQueryUpdated(query: TextFieldValue) {
        _uiState.value = uiState.value.copy(query = query)
    }

    fun onCurrencySelected(currency: Currency) {
        _uiState.value = uiState.value.copy(
            userSelectedCurrency = currency.currencyCode
        )
    }

    fun toggleCurrencyLike(currency: Currency) {
        viewModelScope.launch {
            updateCurrencyFavoriteStateUseCase.execute(currency, !currency.isFavorite)
        }
    }

    fun toggleShowOnlyFavorites() {
        _uiState.value = uiState.value.copy(showOnlyFavorites = !uiState.value.showOnlyFavorites)
    }
}

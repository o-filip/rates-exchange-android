package com.ofilip.exchange_rates.ui.screen.currencySelection

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.core.extensions.filterWithPrev
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.domain.useCase.GetFilteredCurrenciesUseCase
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

data class CurrencySelectionUiState(
    val currencies: List<Currency> = emptyList(),
    val query: TextFieldValue = TextFieldValue(),
    val showOnlyFavorites: Boolean = false,
    val preselectedCurrency: String? = null,
    val filteringErrorMessage: String? = null,
    val wasCurrencySelected: Boolean = false
)

@HiltViewModel
class CurrencySelectionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFilteredCurrencies: GetFilteredCurrenciesUseCase,
    private val currencyRepository: CurrencyRepository,
    private val uiErrorConverter: UiErrorConverter
) : ViewModel() {

    private val mode: CurrencySelectionMode = checkNotNull(savedStateHandle["mode"])

    private val _uiState = MutableStateFlow(CurrencySelectionUiState())
    val uiState: StateFlow<CurrencySelectionUiState> get() = _uiState

    fun init() {
        initPreselectedCurrency()
        initFilteringByQuery()
    }

    private fun initPreselectedCurrency() {
        val preselectedCurrencyFlow = when (mode) {
            CurrencySelectionMode.OverviewCurrency -> currencyRepository.overviewBaseCurrency
            CurrencySelectionMode.ConversionCurrencyFrom -> currencyRepository.conversionCurrencyFrom
            CurrencySelectionMode.ConversionCurrencyTo -> currencyRepository.conversionCurrencyTo
        }
        viewModelScope.launch {
            preselectedCurrencyFlow.first().onSuccess { preselectedCurrency ->
                _uiState.value =
                    uiState.value.copy(preselectedCurrency = preselectedCurrency)
            }

        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initFilteringByQuery() {
        viewModelScope.launch {
            uiState
                .filterWithPrev { prev, current ->
                    // Ignore changes in other fields
                    prev?.query?.text != current.query.text ||
                            prev.showOnlyFavorites != current.showOnlyFavorites
                }
                .flatMapLatest { uiState ->
                    getFilteredCurrencies.execute(uiState.query.text, uiState.showOnlyFavorites)
                }.collect { result ->
                    result.onSuccess { currencies ->
                        _uiState.value = uiState.value.copy(
                            currencies = currencies,
                            filteringErrorMessage = null
                        )
                    }.onFailure {
                        _uiState.value = uiState.value.copy(
                            filteringErrorMessage = uiErrorConverter.convertToText(it)
                        )
                    }
                }
        }
    }

    fun onQueryUpdated(query: TextFieldValue) {
        _uiState.value = uiState.value.copy(query = query)
    }

    fun onCurrencySelected(currency: Currency) {
        viewModelScope.launch {
            when (mode) {
                CurrencySelectionMode.OverviewCurrency ->
                    currencyRepository.setOverviewBaseCurrency(currency.currencyCode)
                CurrencySelectionMode.ConversionCurrencyFrom ->
                    currencyRepository.setConversionCurrencyFrom(currency.currencyCode)
                CurrencySelectionMode.ConversionCurrencyTo ->
                    currencyRepository.setConversionCurrencyTo(currency.currencyCode)
            }
            _uiState.value = uiState.value.copy(wasCurrencySelected = true)
        }
    }

    fun toggleCurrencyLike(currency: Currency) {
        viewModelScope.launch {
            currencyRepository.updateCurrencyFavoriteState(currency, !currency.isFavorite)
        }
    }

    fun toggleShowOnlyFavorites() {
        _uiState.value = uiState.value.copy(showOnlyFavorites = !uiState.value.showOnlyFavorites)
    }
}

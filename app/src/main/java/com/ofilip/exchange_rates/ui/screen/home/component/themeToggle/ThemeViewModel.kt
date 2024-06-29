package com.ofilip.exchange_rates.ui.screen.home.component.themeToggle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ofilip.exchange_rates.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val appPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    val isDarkTheme = appPreferencesRepository.isDarkTheme

    fun setDarkTheme(darkTheme: Boolean) {
        viewModelScope.launch {
            appPreferencesRepository.setIsDarkTheme(darkTheme)
        }
    }
}

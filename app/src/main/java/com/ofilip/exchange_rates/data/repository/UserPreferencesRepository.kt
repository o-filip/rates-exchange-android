package com.ofilip.exchange_rates.data.repository


import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val isDarkTheme: Flow<Boolean?>

    suspend fun setIsDarkTheme(isDarkTheme: Boolean)
}


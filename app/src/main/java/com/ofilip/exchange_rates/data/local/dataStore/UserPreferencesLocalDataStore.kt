package com.ofilip.exchange_rates.data.local.dataStore

import kotlinx.coroutines.flow.Flow

interface UserPreferencesLocalDataStore {
    val darkTheme: Flow<Boolean?>

    suspend fun setDarkTheme(darkTheme: Boolean)
}


package com.ofilip.exchange_rates.data.repository


import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val darkTheme: Flow<Boolean?>

    suspend fun setDarkTheme(darkTheme: Boolean): Result<Unit>
}


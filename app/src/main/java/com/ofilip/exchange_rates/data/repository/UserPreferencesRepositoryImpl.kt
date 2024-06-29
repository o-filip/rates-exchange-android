package com.ofilip.exchange_rates.data.repository

import com.ofilip.exchange_rates.data.local.dataStore.UserPreferencesLocalDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl @Inject constructor(
    private val appProtoStore: UserPreferencesLocalDataStore
) : BaseRepository(), UserPreferencesRepository {
    override val isDarkTheme: Flow<Boolean?> get() = repoFetch { appProtoStore.darkTheme }

    override suspend fun setIsDarkTheme(isDarkTheme: Boolean) = repoDoWithoutResult {
        appProtoStore.setDarkTheme(isDarkTheme)
    }
}

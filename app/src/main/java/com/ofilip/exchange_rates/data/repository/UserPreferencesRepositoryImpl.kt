package com.ofilip.exchange_rates.data.repository

import com.ofilip.exchange_rates.data.local.dataStore.UserPreferencesLocalDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserPreferencesRepositoryImpl @Inject constructor(
    private val appProtoStore: UserPreferencesLocalDataStore
) : BaseRepository(), UserPreferencesRepository {
    override val darkTheme: Flow<Boolean?> get() = repoFetch {  appProtoStore.darkTheme }

    override suspend fun setDarkTheme(darkTheme: Boolean): Result<Unit> = repoDoNoResult {
        appProtoStore.setDarkTheme(darkTheme)
    }
}

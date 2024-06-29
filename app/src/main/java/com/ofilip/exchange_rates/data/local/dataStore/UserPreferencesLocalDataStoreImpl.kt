package com.ofilip.exchange_rates.data.local.dataStore

import com.ofilip.exchange_rates.data.local.protoStore.AppProtoStoreManager
import com.ofilip.exchange_rates.data.local.protoStore.BaseProtoDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class UserPreferencesLocalDataStoreImpl @Inject constructor(
    private val appProtoStore: AppProtoStoreManager
) : UserPreferencesLocalDataStore, BaseProtoDataStore {
    override val darkTheme: Flow<Boolean?> = protoFetch {
        appProtoStore.darkTheme
    }

    override suspend fun setDarkTheme(darkTheme: Boolean) = protoDo {
        appProtoStore.setDarkTheme(darkTheme)
    }
}

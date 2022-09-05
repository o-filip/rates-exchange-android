package com.ofilip.exchange_rates.core.di.binding

import com.ofilip.exchange_rates.data.local.dataStore.CurrencyLocalDataStore
import com.ofilip.exchange_rates.data.local.dataStore.CurrencyLocalDataStoreImpl
import com.ofilip.exchange_rates.data.local.dataStore.CurrencyRateLocalDataStore
import com.ofilip.exchange_rates.data.local.dataStore.CurrencyRateLocalDataStoreImpl
import com.ofilip.exchange_rates.data.local.dataStore.UserPreferencesLocalDataStore
import com.ofilip.exchange_rates.data.local.dataStore.UserPreferencesLocalDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract  class LocalDataStoreBindingModule {

    @Binds
    abstract fun bindCurrencyLocalDataStore(
        localDataStoreImpl: CurrencyLocalDataStoreImpl
    ): CurrencyLocalDataStore

    @Binds
    abstract fun bindCurrencyRateLocalDataStore(
        localDataStoreImpl: CurrencyRateLocalDataStoreImpl
    ): CurrencyRateLocalDataStore

    @Binds
    abstract fun userPreferencesLocalDataStore(
        localDataStoreImpl: UserPreferencesLocalDataStoreImpl
    ): UserPreferencesLocalDataStore
}
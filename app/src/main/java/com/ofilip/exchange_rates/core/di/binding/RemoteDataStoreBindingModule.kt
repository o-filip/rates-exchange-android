package com.ofilip.exchange_rates.core.di.binding

import com.ofilip.exchange_rates.data.remote.dataStore.CurrencyRemoteDataStore
import com.ofilip.exchange_rates.data.remote.dataStore.CurrencyRemoteDataStoreImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataStoreBindingModule {

    @Binds
    abstract fun bindsCurrencyRemoteDataStore(
        currencyRemoteDataStoreImpl: CurrencyRemoteDataStoreImpl
    ): CurrencyRemoteDataStore
}

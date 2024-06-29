package com.ofilip.exchange_rates.core.di.binding

import com.ofilip.exchange_rates.data.repository.CurrencyRateRepository
import com.ofilip.exchange_rates.data.repository.CurrencyRateRepositoryImpl
import com.ofilip.exchange_rates.data.repository.CurrencyRepository
import com.ofilip.exchange_rates.data.repository.CurrencyRepositoryImpl
import com.ofilip.exchange_rates.data.repository.UserPreferencesRepository
import com.ofilip.exchange_rates.data.repository.UserPreferencesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryBindingModule {
    @Binds
    abstract fun bindCurrencyRepository(
        currencyRepositoryImpl: CurrencyRepositoryImpl
    ): CurrencyRepository

    @Binds
    abstract fun bindUserPreferencesRepository(
        userPreferencesRepositoryImpl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    @Binds
    abstract fun bindCurrencyRateRepository(
        currencyRateRepositoryImpl: CurrencyRateRepositoryImpl
    ): CurrencyRateRepository
}

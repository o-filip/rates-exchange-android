package com.ofilip.exchange_rates.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ConstantsModule {

    @BaseRatesCurrency
    @Provides
    fun provideBaseCurrency(): String = "CZK"

    @DefaultConversionFromCurrency
    @Provides
    fun provideDefaultConversionFromCurrency(): String = "USD"

    @DefaultConversionToCurrency
    @Provides
    fun provideDefaultConversionToCurrency(): String = "EUR"

    @CurrencyRemoteFetchLimitMs
    @Provides
    fun provideCurrencyRemoteFetchLimitMs(): Long = 1000 * 60 * 15
}

package com.ofilip.exchange_rates.core.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseCurrencyBeaconApiUrl

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CurrencyBeaconApiKey

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CurrencyBeaconApiWsGenerator

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BaseRatesCurrency

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultConversionFromCurrency

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultConversionToCurrency

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class CurrencyRemoteFetchLimitMs

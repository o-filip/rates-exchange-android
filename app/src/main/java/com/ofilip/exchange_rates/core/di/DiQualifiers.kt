package com.ofilip.exchange_rates.core.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

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

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val dispatcher: AppDispatchers)

enum class AppDispatchers {
    Default,
    IO,
    Main,
}

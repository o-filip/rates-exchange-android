package com.ofilip.exchange_rates.core.di

import com.ofilip.exchange_rates.BuildConfig
import com.ofilip.exchange_rates.data.remote.retrofit.generator.WsGenerator
import com.ofilip.exchange_rates.data.remote.retrofit.generator.WsGeneratorOptions
import com.ofilip.exchange_rates.data.remote.retrofit.interceptor.CurrencyBeaconAuthInterceptor
import com.ofilip.exchange_rates.data.remote.retrofit.service.CurrencyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataStoreModule {

    @BaseCurrencyBeaconApiUrl
    @Provides
    fun provideBaseNewsApiUrl(): String = "https://api.currencybeacon.com/v1/"

    @CurrencyBeaconApiKey
    @Provides
    fun provideBaseApiUrl(): String = BuildConfig.CURRENCY_BEACON_API_KEY

    @CurrencyBeaconApiWsGenerator
    @Provides
    fun provideNewsApiWsGenerator(
        @BaseCurrencyBeaconApiUrl baseUrl: String,
    ): WsGenerator = WsGenerator(apiBaseUrl = baseUrl)

    @Provides
    fun provideCurrencyBeaconApiAuthInterceptor(
        @CurrencyBeaconApiKey apiKey: String,
    ): CurrencyBeaconAuthInterceptor = CurrencyBeaconAuthInterceptor(apiKey)

    @Provides
    fun provideCurrencyService(
        @CurrencyBeaconApiWsGenerator wsGenerator: WsGenerator,
        authInterceptor: CurrencyBeaconAuthInterceptor
    ): CurrencyService = wsGenerator.createService(
        CurrencyService::class.java,
        options = WsGeneratorOptions(
            interceptors = listOf(authInterceptor),
        )
    )

}

package com.ofilip.exchange_rates.core.di

import com.ofilip.exchange_rates.BuildConfig
import com.ofilip.exchange_rates.data.remote.ktor.utils.KtorLogger
import com.ofilip.exchange_rates.data.remote.ktor.generator.KtorWsGenerator
import com.ofilip.exchange_rates.data.remote.ktor.service.CurrencyService
import com.ofilip.exchange_rates.data.remote.ktor.interceptor.CurrencyBeaconAuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataStoreModule {

    @BaseCurrencyBeaconApiUrl
    @Provides
    fun provideBaseNewsApiUrl(): String = "https://api.currencybeacon.com/v1/"

    @CurrencyBeaconApiKey
    @Provides
    fun provideBaseApiUrl(): String = BuildConfig.CURRENCY_BEACON_API_KEY

    @Provides
    fun provideNewsApiWsGenerator(): KtorWsGenerator = KtorWsGenerator()

    @Provides
    fun provideCurrencyBeaconApiAuthInterceptor(
        @CurrencyBeaconApiKey apiKey: String,
    ): CurrencyBeaconAuthInterceptor = CurrencyBeaconAuthInterceptor(apiKey)

    @Provides
    fun provideKtorLogger(): KtorLogger = KtorLogger()

    @CurrencyBeaconClient
    @Provides
    fun provideHttpClient(
        wsGenerator: KtorWsGenerator,
        authInterceptor: CurrencyBeaconAuthInterceptor,
        @BaseCurrencyBeaconApiUrl baseUrl: String,
        logger: KtorLogger
    ): HttpClient = wsGenerator.createClient(
        baseUrl,
        listOf(authInterceptor),
        logger
    )

    @Provides
    fun provideCurrencyService(
        @CurrencyBeaconClient client: HttpClient
    ): CurrencyService = CurrencyService(client)
}

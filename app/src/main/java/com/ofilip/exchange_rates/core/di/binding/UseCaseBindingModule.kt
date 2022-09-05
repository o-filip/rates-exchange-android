package com.ofilip.exchange_rates.core.di.binding

import com.ofilip.exchange_rates.domain.useCase.ApplyConversionRateToAmountUseCase
import com.ofilip.exchange_rates.domain.useCase.ApplyConversionRateToAmountUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.ConvertCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.ConvertCurrencyUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.GetFilteredCurrenciesUseCase
import com.ofilip.exchange_rates.domain.useCase.GetFilteredCurrenciesUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.GetInternetConnectionStatusUseCase
import com.ofilip.exchange_rates.domain.useCase.GetInternetConnectionStatusUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.GetRatesForOverviewUseCase
import com.ofilip.exchange_rates.domain.useCase.GetRatesForOverviewUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.GetBaseRatesOfAllCurrenciesUseCase
import com.ofilip.exchange_rates.domain.useCase.GetBaseRatesOfAllCurrenciesUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.GetCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.GetCurrencyUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.InitializeAppUseCase
import com.ofilip.exchange_rates.domain.useCase.InitializeAppUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseBindingModule {
    @Binds
    abstract fun bindConvertCurrencyUseCase(
        useCaseImpl: ConvertCurrencyUseCaseImpl
    ): ConvertCurrencyUseCase

    @Binds
    abstract fun bindGetFilteredCurrenciesUseCase(
        useCaseImpl: GetFilteredCurrenciesUseCaseImpl
    ): GetFilteredCurrenciesUseCase

    @Binds
    abstract fun bindGetInternetConnectionStatusUseCase(
        useCaseImpl: GetInternetConnectionStatusUseCaseImpl
    ): GetInternetConnectionStatusUseCase

    @Binds
    abstract fun bindGetOverviewRatesUseCase(
        useCaseImpl: GetRatesForOverviewUseCaseImpl
    ): GetRatesForOverviewUseCase

    @Binds
    abstract fun bindInitializeAppUseCase(
        useCaseImpl: InitializeAppUseCaseImpl
    ): InitializeAppUseCase

    @Binds
    abstract fun bindGetRatesOfAllCurrenciesUseCase(
        useCaseImpl: GetBaseRatesOfAllCurrenciesUseCaseImpl
    ): GetBaseRatesOfAllCurrenciesUseCase

    @Binds
    abstract fun bindApplyConversionRateToAmountUseCase(
        useCaseImpl: ApplyConversionRateToAmountUseCaseImpl
    ): ApplyConversionRateToAmountUseCase

    @Binds
    abstract fun bindGetCurrencyUseCase(
        useCaseImpl: GetCurrencyUseCaseImpl
    ): GetCurrencyUseCase


}
package com.ofilip.exchange_rates.core.di.binding

import com.ofilip.exchange_rates.domain.useCase.GetInternetConnectionStatusUseCase
import com.ofilip.exchange_rates.domain.useCase.GetInternetConnectionStatusUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.InitializeAppUseCase
import com.ofilip.exchange_rates.domain.useCase.InitializeAppUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.conversion.ApplyConversionRateToAmountUseCase
import com.ofilip.exchange_rates.domain.useCase.conversion.ApplyConversionRateToAmountUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.conversion.ConvertCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.conversion.ConvertCurrencyUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.currency.GetConversionCurrencyFromUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.GetConversionCurrencyFromUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.currency.GetConversionCurrencyToUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.GetConversionCurrencyToUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.currency.GetCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.GetCurrencyUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.currency.GetFilteredCurrenciesUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.GetFilteredCurrenciesUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.currency.GetOverviewBaseCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.GetOverviewBaseCurrencyUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.currency.SetConversionCurrencyFromUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.SetConversionCurrencyFromUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.currency.SetConversionCurrencyToUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.SetConversionCurrencyToUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.currency.SetOverviewBaseCurrencyUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.SetOverviewBaseCurrencyUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.currency.UpdateCurrencyFavoriteStateUseCase
import com.ofilip.exchange_rates.domain.useCase.currency.UpdateCurrencyFavoriteStateUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.rate.GetBaseRatesOfAllCurrenciesUseCase
import com.ofilip.exchange_rates.domain.useCase.rate.GetBaseRatesOfAllCurrenciesUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.rate.GetRatesForOverviewUseCase
import com.ofilip.exchange_rates.domain.useCase.rate.GetRatesForOverviewUseCaseImpl
import com.ofilip.exchange_rates.domain.useCase.rateTimeSeries.GetRatesTimeSeriesUseCase
import com.ofilip.exchange_rates.domain.useCase.rateTimeSeries.GetRatesTimeSeriesUseCaseImpl
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

    @Binds
    abstract fun bindGetRatesTimeSeriesUseCase(
        useCaseImpl: GetRatesTimeSeriesUseCaseImpl
    ): GetRatesTimeSeriesUseCase

    @Binds
    abstract fun bindSetConversionCurrencyToUseCase(
        useCaseImpl: SetConversionCurrencyToUseCaseImpl
    ): SetConversionCurrencyToUseCase

    @Binds
    abstract fun bindSetConversionCurrencyFromUseCase(
        useCaseImpl: SetConversionCurrencyFromUseCaseImpl
    ): SetConversionCurrencyFromUseCase

    @Binds
    abstract fun bindSetOverviewBaseCurrencyUseCase(
        useCaseImpl: SetOverviewBaseCurrencyUseCaseImpl
    ): SetOverviewBaseCurrencyUseCase

    @Binds
    abstract fun bindUpdateCurrencyFavoriteStateUseCase(
        useCaseImpl: UpdateCurrencyFavoriteStateUseCaseImpl
    ): UpdateCurrencyFavoriteStateUseCase

    @Binds
    abstract fun bindGetOverviewBaseCurrencyUseCase(
        useCaseImpl: GetOverviewBaseCurrencyUseCaseImpl
    ): GetOverviewBaseCurrencyUseCase

    @Binds
    abstract fun bindGetConversionCurrencyFromUseCase(
        useCaseImpl: GetConversionCurrencyFromUseCaseImpl
    ): GetConversionCurrencyFromUseCase

    @Binds
    abstract fun bindGetConversionCurrencyToUseCase(
        useCaseImpl: GetConversionCurrencyToUseCaseImpl
    ): GetConversionCurrencyToUseCase
}

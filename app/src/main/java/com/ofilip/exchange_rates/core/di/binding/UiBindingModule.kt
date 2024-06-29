package com.ofilip.exchange_rates.core.di.binding

import com.ofilip.exchange_rates.ui.util.ChartHelper
import com.ofilip.exchange_rates.ui.util.ChartHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UiBindingModule {
    @Binds
    abstract fun bindChartHelper(
        chartHelperImpl: ChartHelperImpl
    ): ChartHelper
}

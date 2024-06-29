package com.ofilip.exchange_rates.core.di.binding

import com.ofilip.exchange_rates.data.convert.CurrencyConverter
import com.ofilip.exchange_rates.data.convert.CurrencyConverterImpl
import com.ofilip.exchange_rates.data.convert.CurrencyRateConverter
import com.ofilip.exchange_rates.data.convert.CurrencyRateConverterImpl
import com.ofilip.exchange_rates.data.convert.RatesTimeSeriesConverter
import com.ofilip.exchange_rates.data.convert.RatesTimeSeriesConverterImpl
import com.ofilip.exchange_rates.ui.util.UiErrorConverter
import com.ofilip.exchange_rates.ui.util.UiErrorConverterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ConverterBindingModule {

    @Binds
    abstract fun bindsCurrencyConverter(
        converter: CurrencyConverterImpl
    ): CurrencyConverter

    @Binds
    abstract fun bindsCurrencyRateConverter(
        converter: CurrencyRateConverterImpl
    ): CurrencyRateConverter

    @Binds
    abstract fun bindsUiErrorConverter(
        converter: UiErrorConverterImpl
    ): UiErrorConverter

    @Binds
    abstract fun bindsRatesTimeSeriesConverter(
        converter: RatesTimeSeriesConverterImpl
    ): RatesTimeSeriesConverter

}

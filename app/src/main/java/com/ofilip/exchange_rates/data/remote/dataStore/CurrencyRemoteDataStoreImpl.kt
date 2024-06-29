package com.ofilip.exchange_rates.data.remote.dataStore


import com.ofilip.exchange_rates.data.remote.model.CurrencyRatesRemoteModel
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import com.ofilip.exchange_rates.data.remote.model.RatesTimeSeriesRemoteModel
import com.ofilip.exchange_rates.data.remote.retrofit.dataStore.BaseRetrofitDataStore
import com.ofilip.exchange_rates.data.remote.retrofit.service.CurrencyService
import javax.inject.Inject
import org.joda.time.DateTime

class CurrencyRemoteDataStoreImpl @Inject constructor(
    private val currencyService: CurrencyService,
) : CurrencyRemoteDataStore, BaseRetrofitDataStore {

    override suspend fun getLatestRates(
        baseCurrencyCode: String,
        symbols: List<String>
    ): CurrencyRatesRemoteModel = retrofitFetch {
        currencyService.getLatestRates(baseCurrencyCode, symbols.joinToString(","))
    }

    override suspend fun getAllCurrencies(): List<CurrencyRemoteModel> =
        retrofitFetchWrappedResponse {
            currencyService.getAllCurrencies()
        }

    override suspend fun getRatesTimeSeries(
        startDate: DateTime,
        endDate: DateTime,
        base: String,
        symbols: List<String>?
    ): RatesTimeSeriesRemoteModel = retrofitFetch {
        currencyService.getTimeSeries(
            startDate,
            endDate,
            base,
            symbols?.joinToString(",")
        )
    }
}

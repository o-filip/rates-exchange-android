package com.ofilip.exchange_rates.data.remote.dataStore


import com.ofilip.exchange_rates.data.remote.ktor.dataStore.BaseKtorDataStore
import com.ofilip.exchange_rates.data.remote.ktor.service.CurrencyService
import com.ofilip.exchange_rates.data.remote.model.CurrencyRatesRemoteModel
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import com.ofilip.exchange_rates.data.remote.model.RatesTimeSeriesRemoteModel
import org.joda.time.DateTime
import javax.inject.Inject

class CurrencyRemoteDataStoreImpl @Inject constructor(
    private val currencyService: CurrencyService,
) : CurrencyRemoteDataStore, BaseKtorDataStore {

    override suspend fun getLatestRates(
        baseCurrencyCode: String,
        symbols: List<String>
    ): CurrencyRatesRemoteModel = ktorFetch {
        currencyService.getLatestRates(baseCurrencyCode, symbols.joinToString(","))
    }

    override suspend fun getAllCurrencies(): List<CurrencyRemoteModel> =
        ktorFetchWrappedResponse {
            currencyService.getAllCurrencies()
        }

    override suspend fun getRatesTimeSeries(
        startDate: DateTime,
        endDate: DateTime,
        base: String,
        symbols: List<String>?
    ): RatesTimeSeriesRemoteModel = ktorFetch {
        currencyService.getTimeSeries(
            startDate,
            endDate,
            base,
            symbols?.joinToString(",")
        )
    }
}

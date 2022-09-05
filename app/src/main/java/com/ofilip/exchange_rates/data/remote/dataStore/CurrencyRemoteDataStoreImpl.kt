package com.ofilip.exchange_rates.data.remote.dataStore


import com.ofilip.exchange_rates.data.remote.model.CurrencyRatesRemoteModel
import com.ofilip.exchange_rates.data.remote.model.CurrencyRemoteModel
import com.ofilip.exchange_rates.data.remote.retrofit.dataStore.BaseRetrofitDataStore
import com.ofilip.exchange_rates.data.remote.retrofit.service.CurrencyService
import javax.inject.Inject

class CurrencyRemoteDataStoreImpl @Inject constructor(
    private val currencyService: CurrencyService,
) : CurrencyRemoteDataStore, BaseRetrofitDataStore {

    override suspend fun getLatestRates(
        baseCurrencyCode: String,
        symbols: List<String>
    ): CurrencyRatesRemoteModel = retrofitFetch {
        currencyService.getLatestRates(baseCurrencyCode, symbols.joinToString(","))
    }

    override suspend fun getAllCurrencies(): List<CurrencyRemoteModel> = retrofitFetchWrappedResponse {
        currencyService.getAllCurrencies()
    }
}
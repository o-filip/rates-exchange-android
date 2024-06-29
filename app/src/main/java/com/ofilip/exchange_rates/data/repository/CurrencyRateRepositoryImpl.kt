package com.ofilip.exchange_rates.data.repository

import com.ofilip.exchange_rates.core.di.AppDispatchers
import com.ofilip.exchange_rates.core.di.BaseRatesCurrency
import com.ofilip.exchange_rates.core.di.CurrencyRemoteFetchLimitMs
import com.ofilip.exchange_rates.core.di.Dispatcher
import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.core.entity.RatesTimeSeriesItem
import com.ofilip.exchange_rates.core.network.ConnectivityStatusHelper
import com.ofilip.exchange_rates.data.convert.CurrencyRateConverter
import com.ofilip.exchange_rates.data.convert.RatesTimeSeriesConverter
import com.ofilip.exchange_rates.data.local.dataStore.CurrencyRateLocalDataStore
import com.ofilip.exchange_rates.data.remote.dataStore.CurrencyRemoteDataStore
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import org.joda.time.DateTime

@Singleton
class CurrencyRateRepositoryImpl @Inject constructor(
    private val currencyRemoteDataStore: CurrencyRemoteDataStore,
    private val currencyRateLocalDataStore: CurrencyRateLocalDataStore,
    private val connectivityStatusHelper: ConnectivityStatusHelper,
    private val currencyRateConverter: CurrencyRateConverter,
    private val ratesTimeSeriesConverter: RatesTimeSeriesConverter,
    @BaseRatesCurrency
    private val baseCurrency: String,
    @CurrencyRemoteFetchLimitMs
    private val currencyRemoteFetchLimitMs: Long,
    @Dispatcher(AppDispatchers.IO) private val dispatcher: CoroutineDispatcher
) : BaseRepository(), CurrencyRateRepository {

    override val lastCurrencyRateLoadTimestampMs: Flow<Long>
        get() = repoFetch { currencyRateLocalDataStore.lastRatesLoadTimestampMs }

    override fun getRates(currencyCodes: List<String>): Flow<List<CurrencyRate>> =
        fetchCachedResource(
            shouldFetchFromRemote = {
                connectivityStatusHelper.isConnected.value &&
                        currencyRateLocalDataStore.lastRatesLoadTimestampMs.first() +
                        currencyRemoteFetchLimitMs < System.currentTimeMillis()
            },
            fetchFromRemote = {
                currencyRemoteDataStore.getLatestRates(baseCurrency, currencyCodes)
            },
            fetchFromLocal = { currencyRateLocalDataStore.getAll() },
            store = { newItems ->
                deleteAllRates()
                insertRates(newItems.rates.entries.map {
                    currencyRateConverter.remoteToEntity(it.key, it.value)
                })
            },
            onSuccessfulRemoteFetch = { updateLastRatesLoadTimestampToNow() }
        ).flowOn(dispatcher)

    private suspend fun insertRates(rates: List<CurrencyRate>): Unit = repoDoWithoutResult {
        currencyRateLocalDataStore.insert(rates)
    }

    private suspend fun deleteAllRates() = repoDoWithoutResult {
        currencyRateLocalDataStore.deleteAll()
    }

    private suspend fun updateLastRatesLoadTimestampToNow() = repoDoWithoutResult {
        currencyRateLocalDataStore.setLastRatesLoadTimestampMs(System.currentTimeMillis())
    }

    override suspend fun getRatesTimeSeries(
        startDate: DateTime,
        endDate: DateTime,
        baseCurrencyCode: String,
        currencyCodes: List<String>
    ): List<RatesTimeSeriesItem> = repoFetchSuspend {
        currencyRemoteDataStore.getRatesTimeSeries(
            startDate,
            endDate,
            baseCurrencyCode,
            currencyCodes
        ).let {
            ratesTimeSeriesConverter.convertRemoteToEntity(it)
        }
    }
}

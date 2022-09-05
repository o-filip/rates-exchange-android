package com.ofilip.exchange_rates.data.local.dataStore

import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.data.local.protoStore.AppProtoStoreManager
import com.ofilip.exchange_rates.data.local.protoStore.BaseProtoDataStore
import com.ofilip.exchange_rates.data.local.room.dao.CurrencyDao
import com.ofilip.exchange_rates.data.local.room.dataStore.BaseRoomDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CurrencyLocalDataStoreImpl @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val appProtoStore: AppProtoStoreManager,
) : CurrencyLocalDataStore, BaseRoomDataStore, BaseProtoDataStore {
    override fun getAll(textQuery: String, onlyFavorites: Boolean): Flow<List<Currency>> = roomFetch {
        currencyDao.getAll(textQuery, onlyFavorites)
    }

    override fun getByCode(currencyCode: String): Flow<Currency> = roomFetch {
        currencyDao.getByCode(currencyCode)
    }

    override suspend fun insert(currencies: List<Currency>) = roomDo {
        currencyDao.insert(currencies)
    }

    override suspend fun update(currency: Currency) = roomDo {
        currencyDao.update(currency)
    }

    override suspend fun delete(currencies: List<Currency>) = roomDo {
        currencyDao.delete(currencies)
    }

    override val overviewBaseCurrency: Flow<String> =  protoFetch {
        appProtoStore.overviewBaseCurrency
    }

    override val conversionCurrencyFrom: Flow<String> = protoFetch {
        appProtoStore.conversionCurrencyFrom
    }

    override val conversionCurrencyTo: Flow<String> = protoFetch {
        appProtoStore.conversionCurrencyTo
    }

    override suspend fun setOverviewBaseCurrency(currencyCode: String) = roomDo {
        appProtoStore.setOverviewBaseCurrency(currencyCode)
    }

    override suspend fun setConversionCurrencyFrom(currencyCode: String) = roomDo {
        appProtoStore.setConversionCurrencyFrom(currencyCode)
    }

    override suspend fun setConversionCurrencyTo(currencyCode: String) = roomDo {
        appProtoStore.setConversionCurrencyTo(currencyCode)
    }
}
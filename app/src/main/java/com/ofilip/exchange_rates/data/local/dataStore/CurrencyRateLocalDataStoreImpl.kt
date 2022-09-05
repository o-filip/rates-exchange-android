package com.ofilip.exchange_rates.data.local.dataStore

import com.ofilip.exchange_rates.core.entity.CurrencyRate
import com.ofilip.exchange_rates.data.local.protoStore.AppProtoStoreManager
import com.ofilip.exchange_rates.data.local.protoStore.BaseProtoDataStore
import com.ofilip.exchange_rates.data.local.room.dao.CurrencyRateDao
import com.ofilip.exchange_rates.data.local.room.dataStore.BaseRoomDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CurrencyRateLocalDataStoreImpl @Inject constructor(
    private val currencyRateDao: CurrencyRateDao,
    private val appProtoStore: AppProtoStoreManager,
) : CurrencyRateLocalDataStore, BaseRoomDataStore, BaseProtoDataStore {
    override fun getAll(): Flow<List<CurrencyRate>> = roomFetch {
        currencyRateDao.getAll()
    }

    override suspend fun insert(currencyRates: List<CurrencyRate>) = roomDo {
        currencyRateDao.insert(currencyRates)
    }

    override suspend fun deleteAll() = roomDo {
        currencyRateDao.deleteAll()
    }

    override val lastRatesLoadTimestampMs: Flow<Long> = protoFetch {
        appProtoStore.lastRatesLoadTimestampMs
    }

    override suspend fun setLastRatesLoadTimestampMs(timestamp: Long) = protoDo {
        appProtoStore.setLastRatesLoadTimestampMs(timestamp)
    }
}
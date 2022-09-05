package com.ofilip.exchange_rates.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ofilip.exchange_rates.data.local.room.dao.CurrencyDao
import com.ofilip.exchange_rates.data.local.room.dao.CurrencyRateDao
import com.ofilip.exchange_rates.core.entity.Currency
import com.ofilip.exchange_rates.core.entity.CurrencyRate

@Database(
    entities = [
        Currency::class,
        CurrencyRate::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseConvertors::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun currencyDao(): CurrencyDao

    abstract fun currencyRateDao(): CurrencyRateDao
}

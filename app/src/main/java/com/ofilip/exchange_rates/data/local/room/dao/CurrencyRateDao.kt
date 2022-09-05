package com.ofilip.exchange_rates.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ofilip.exchange_rates.core.entity.CurrencyRate
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyRateDao {

    @Query("SELECT * FROM currency_rate ORDER BY currency")
    fun getAll(): Flow<List<CurrencyRate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencyRates: List<CurrencyRate>)

    @Query("DELETE FROM currency_rate")
    suspend fun deleteAll()
}

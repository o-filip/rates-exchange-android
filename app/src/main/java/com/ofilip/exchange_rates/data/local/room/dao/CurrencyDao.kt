package com.ofilip.exchange_rates.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ofilip.exchange_rates.core.entity.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {

    @Query(
        "SELECT * FROM currency " +
            "WHERE (" +
            "      currencyCode LIKE '%' || :textQuery || '%' " +
            "      OR currencyName LIKE '%' || :textQuery || '%' " +
            "   ) AND " +
            "      CASE " +
            "         WHEN :onlyFavorites THEN isFavorite = 1 " +
            "         ELSE 1 " +
            "      END " +
            "ORDER BY currencyCode"
    )
    fun getAll(textQuery: String = "", onlyFavorites: Boolean = false): Flow<List<Currency>>

    @Query("SELECT * FROM currency WHERE currencyCode = :currencyCode")
    fun getByCode(currencyCode: String): Flow<Currency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(currencies: List<Currency>)

    @Update
    suspend fun update(currency: Currency)

    @Delete
    suspend fun delete(currencies: List<Currency>)
}

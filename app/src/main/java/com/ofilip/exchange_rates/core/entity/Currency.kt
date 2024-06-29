package com.ofilip.exchange_rates.core.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "currency"
)
data class Currency(
    @PrimaryKey
    val currencyCode: String,
    val currencyName: String?,
    val isFavorite: Boolean,
    val numberCode: String?,
    val precision: Int?,
    val symbol: String?,
    val symbolFirst: Boolean?,
    val thousandsSeparator: String?,
    val decimalSeparator: String?,
)

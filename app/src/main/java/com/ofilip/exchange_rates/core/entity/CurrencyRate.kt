package com.ofilip.exchange_rates.core.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "currency_rate"
)
data class CurrencyRate(
    @PrimaryKey
    val currency: String,
    val rate: Double?
)

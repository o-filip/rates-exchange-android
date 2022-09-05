package com.ofilip.exchange_rates.core.di

import android.content.Context
import androidx.room.Room
import com.ofilip.exchange_rates.data.local.room.AppDatabase
import com.ofilip.exchange_rates.data.local.room.dao.CurrencyDao
import com.ofilip.exchange_rates.data.local.room.dao.CurrencyRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataStoreModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext
        context: Context
    ): AppDatabase = Room.databaseBuilder(
        context, AppDatabase::class.java, "database-exchange-rates"
    ).fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideCurrencyDao(
        appDatabase: AppDatabase
    ): CurrencyDao = appDatabase.currencyDao()

    @Provides
    fun provideActivityDao(
        appDatabase: AppDatabase
    ): CurrencyRateDao = appDatabase.currencyRateDao()
}

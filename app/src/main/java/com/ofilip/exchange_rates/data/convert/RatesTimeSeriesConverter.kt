package com.ofilip.exchange_rates.data.convert

import com.ofilip.exchange_rates.core.entity.RatesTimeSeriesItem
import com.ofilip.exchange_rates.data.remote.model.RatesTimeSeriesRemoteModel
import javax.inject.Inject
import org.joda.time.DateTime

interface RatesTimeSeriesConverter {
    fun convertRemoteToEntity(
        remote: RatesTimeSeriesRemoteModel
    ): List<RatesTimeSeriesItem>
}

class RatesTimeSeriesConverterImpl @Inject constructor() : RatesTimeSeriesConverter {
    override fun convertRemoteToEntity(remote: RatesTimeSeriesRemoteModel): List<RatesTimeSeriesItem> =
        remote.response.entries.map {
            RatesTimeSeriesItem(
//                date = it.key,
                date = DateTime.now(),
                rates = it.value
            )
        }
}
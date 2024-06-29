package com.ofilip.exchange_rates.data.convert

import com.ofilip.exchange_rates.data.remote.model.RatesTimeSeriesRemoteModel
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RatesTimeSeriesConverterImplTest {

    private val converter: RatesTimeSeriesConverter = RatesTimeSeriesConverterImpl()

    @Test
    fun testConvertRemoteToEntity() {
        // Given
        val remote = RatesTimeSeriesRemoteModel(
            response = mapOf(
                DateTime.now() to mapOf("USD" to 1.0)
            )
        )

        // When
        val entity = converter.convertRemoteToEntity(remote)

        // Then
        assertEquals(1, entity.size)
        assertEquals(remote.response.keys.first(), entity.first().date)
        assertEquals(remote.response.values.first(), entity.first().rates)
    }
}

package com.ofilip.exchange_rates.data.remote.retrofit.converter

import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.KeyDeserializer
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.ISODateTimeFormat

class DateDeserializer : KeyDeserializer() {
    override fun deserializeKey(key: String?, ctxt: DeserializationContext?): Any {
        return ISODateTimeFormat.dateTimeParser().parseDateTime(key)
    }
}
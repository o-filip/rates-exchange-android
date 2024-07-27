package com.ofilip.exchange_rates.data.local.room

import androidx.room.TypeConverter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.type.TypeReference


class DatabaseConvertors {

    @TypeConverter
    fun listOfStringStringFromJson(json: String?): List<String>? {
        if (json == null) return null
        return ObjectMapper().readValue(json, object : TypeReference<List<String>>() {})
    }

    @TypeConverter
    fun listOfStringStringToJson(map: List<String>?): String? {
        if (map == null) return null
        return ObjectMapper().writeValueAsString(map)
    }
}

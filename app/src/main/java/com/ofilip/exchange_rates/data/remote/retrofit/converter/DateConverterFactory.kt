package com.ofilip.exchange_rates.data.remote.retrofit.converter

import java.lang.reflect.Type
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import retrofit2.Converter
import retrofit2.Retrofit

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class DateQuery

class DateConverterFactory: Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? {
        if (annotations.any { it is DateQuery}) {
            return Converter<Any, String> { value ->
                if (value !is DateTime) {
                    throw IllegalArgumentException("Value must be a DateTime")
                }
                DateTimeFormat.forPattern("yyyy-MM-dd").print(value)
            }
        }
        return null
    }
}
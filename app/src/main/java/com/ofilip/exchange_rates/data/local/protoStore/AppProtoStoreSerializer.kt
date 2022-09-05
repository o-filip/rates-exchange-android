package com.ofilip.exchange_rates.data.local.protoStore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.ofilip.exchange_rates.AppProtoStore

import java.io.InputStream
import java.io.OutputStream

/**
 * Serializer for [AppProtoStore] stored in proto store
 */
object AppProtoStoreSerializer : Serializer<AppProtoStore> {
    override val defaultValue: AppProtoStore = AppProtoStore.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppProtoStore {
        try {
            return AppProtoStore.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(
        t: AppProtoStore,
        output: OutputStream
    ) = t.writeTo(output)
}

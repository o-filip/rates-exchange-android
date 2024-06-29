package com.ofilip.exchange_rates.data.util

/**
 * Helper class used to keep in sync between locally store data and data stored in remote storage.
 * It compares list of currently stored data in local store and new data fetched from
 * remote, and generate:
 *  - new lists of data that should be inserted into local data store
 *  - list of updated local data that should be updated in local data store
 *  - list of data, that is not presented in remote anymore and should be deleted from local store
 */
class RemoteAndLocalModelDiffer<LocalType, RemoteType>(
    /**
     * Items stored in local data store
     */
    val localItems: List<LocalType>,

    /**
     * Items fetched from remote
     */
    val remoteItems: List<RemoteType>,

    /**
     * Returns true if local and remote items are the same
     */
    val areItemsSame: (localItem: LocalType, remoteItem: RemoteType) -> Boolean,

    /**
     * Update local item by data from remote item
     */
    val updateLocalItem: (localItem: LocalType, remoteItem: RemoteType) -> LocalType,

    /**
     * Create new local item based on remote item
     */
    val createLocalItem: (remoteItem: RemoteType) -> LocalType
) {
    /**
     * Generates list of data that is in [remoteItems] but not in [localItems] and
     * should be inserted into local data store
     */
    val itemsToInsert: List<LocalType> by lazy {
        remoteItems.mapNotNull { remoteItem ->
            localItems.find { localItem -> areItemsSame(localItem, remoteItem) }
                .let {
                    if (it == null) createLocalItem(remoteItem)
                    else null
                }
        }
    }

    /**
     * Generates list of data that is in both [remoteItems] and [localItems] and should be updated
     * in local storage
     */
    val itemsToUpdate: List<LocalType> by lazy {
        remoteItems.mapNotNull { remoteItem ->
            localItems.find { localItem -> areItemsSame(localItem, remoteItem) }
                .let {
                    if (it != null) updateLocalItem(it, remoteItem)
                    else null
                }
        }
    }

    /**
     * Generates list of data that is in  [localItems] but not in  [remoteItems] and should be deleted
     * from local storage
     */
    val itemsToDelete: List<LocalType> by lazy {
        localItems.filter {
            remoteItems.find { remoteItem ->
                areItemsSame(it, remoteItem)
            } == null
        }
    }

    /**
     * Generates data that should be updated or inserted into local data store
     */
    val itemsToUpdateOrInsert: List<LocalType> by lazy {
        itemsToInsert.toMutableList().apply { addAll(itemsToUpdate) }
    }
}

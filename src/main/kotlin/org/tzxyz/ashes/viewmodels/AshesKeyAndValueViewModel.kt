package org.tzxyz.ashes.viewmodels

import org.tzxyz.ashes.models.*
import tornadofx.ItemViewModel

class AshesStringValueViewModel: ItemViewModel<AshesKeyStringValue>() {
    val key = bind(AshesKeyStringValue::key)
    val type = bind(AshesKeyStringValue::type)
    val ttl = bind(AshesKeyStringValue::ttl)
    val cost = bind(AshesKeyStringValue::cost)
    val value = bind(AshesKeyStringValue::value)
}
class AshesListValueViewModel: ItemViewModel<AshesKeyListValue>()
class AshesHashValueViewModel: ItemViewModel<AshesKeyHashValue>()
class AshesSetValueViewModel: ItemViewModel<AshesKeySetValue>()
class AshesSortedSetValueViewModel: ItemViewModel<AshesKeySortedSetValue>()
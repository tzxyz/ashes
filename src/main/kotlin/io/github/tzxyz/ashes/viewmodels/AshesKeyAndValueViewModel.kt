package io.github.tzxyz.ashes.viewmodels

import io.github.tzxyz.ashes.models.*
import tornadofx.ItemViewModel

class AshesStringValueViewModel: ItemViewModel<AshesKeyStringValue>() {
    val key = bind(AshesKeyStringValue::key)
    val type = bind(AshesKeyStringValue::type)
    val ttl = bind(AshesKeyStringValue::ttl)
    val cost = bind(AshesKeyStringValue::cost)
    val value = bind(AshesKeyStringValue::value)
}
class AshesListValueViewModel: ItemViewModel<AshesKeyListValue>() {
    val key = bind(AshesKeyListValue::key)
    val type = bind(AshesKeyListValue::type)
    val ttl = bind(AshesKeyListValue::ttl)
    val cost = bind(AshesKeyListValue::cost)
    val value = bind(AshesKeyListValue::value)
}
class AshesHashValueViewModel: ItemViewModel<AshesKeyHashValue>()
class AshesSetValueViewModel: ItemViewModel<AshesKeySetValue>()
class AshesSortedSetValueViewModel: ItemViewModel<AshesKeySortedSetValue>()
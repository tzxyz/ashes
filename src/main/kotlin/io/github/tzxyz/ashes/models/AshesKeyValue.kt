package io.github.tzxyz.ashes.models

import javafx.collections.ObservableList

open class AshesKeyValue(
    open val key: String,
    open val type: String,
    open val ttl: Long,
    open val cost: Long,
    open val value: Any)

class AshesKeyStringValue(
        override val key: String,
        override val type: String,
        override val ttl: Long,
        override val cost: Long,
        override val value: String): AshesKeyValue(key, type, ttl, cost, value)

class AshesKeyHashValue(
        override val key: String,
        override val type: String,
        override val ttl: Long,
        override val cost: Long,
        override val value: ObservableList<Pair<String, String>>): AshesKeyValue(key, type, ttl, cost, value)

class AshesKeyListValue(
        override val key: String,
        override val type: String,
        override val ttl: Long,
        override val cost: Long,
        override val value: ObservableList<String>): AshesKeyValue(key, type, ttl, cost, value)


class AshesKeySetValue(
        override val key: String,
        override val type: String,
        override val ttl: Long,
        override val cost: Long,
        override val value: ObservableList<String>): AshesKeyValue(key, type, ttl, cost, value)

class AshesKeySortedSetValue(
        override val key: String,
        override val type: String,
        override val ttl: Long,
        override val cost: Long,
        override val value: ObservableList<Pair<Double, String>>): AshesKeyValue(key, type, ttl, cost, value)
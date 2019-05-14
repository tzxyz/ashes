package org.tzxyz.ashes.models

open class AshesKeyAndValue(
    open val key: String,
    open val type: String,
    open val ttl: Long,
    open val cost: Long,
    open val value: Any)

class AshesKeyAndStringValue(
        override val key: String,
        override val type: String,
        override val ttl: Long,
        override val cost: Long,
        override val value: String): AshesKeyAndValue(key, type, ttl, cost, value)

class AshesKeyAndHashValue(
        override val key: String,
        override val type: String,
        override val ttl: Long,
        override val cost: Long,
        override val value: List<Pair<String, String>>): AshesKeyAndValue(key, type, ttl, cost, value)

class AshesKeyAndListValue(
        override val key: String,
        override val type: String,
        override val ttl: Long,
        override val cost: Long,
        override val value: List<String>): AshesKeyAndValue(key, type, ttl, cost, value)


class AshesKeyAndSetValue(
        override val key: String,
        override val type: String,
        override val ttl: Long,
        override val cost: Long,
        override val value: List<Pair<Int, String>>): AshesKeyAndValue(key, type, ttl, cost, value)

class AshesKeyAndSortedSetValue(
        override val key: String,
        override val type: String,
        override val ttl: Long,
        override val cost: Long,
        override val value: List<Pair<Double, String>>): AshesKeyAndValue(key, type, ttl, cost, value)
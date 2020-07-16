package io.github.tzxyz.ashes.controllers

import io.github.tzxyz.ashes.fragments.AshesNewKey
import io.github.tzxyz.ashes.global.Current
import io.github.tzxyz.ashes.models.*
import io.github.tzxyz.ashes.redis.AshesRedisClientFactory
import tornadofx.asObservable

class AshesKeyController: AshesBaseController() {

    private fun client() = AshesRedisClientFactory.get(Current.getConnection())

    fun getKeyAndValue(key: String): AshesKeyValue {
        val ttl = client().ttl(key)
        val type =  client().type(key)
        when(type) {
            "string" -> {
                val now = System.currentTimeMillis()
                val result = client().get(key)
                val cost = System.currentTimeMillis() - now
                return AshesKeyStringValue(key, type, ttl, cost, result)
            }
            "list" -> {
                val now = System.currentTimeMillis()
                val length = client().llen(key)
                val result = client().lrange(key, 0, length)
                val cost = System.currentTimeMillis() - now
                return AshesKeyListValue(key, type, ttl, cost, result.asObservable())
            }
            "hash" -> {
                val now = System.currentTimeMillis()
                val result = client().hgetAll(key).toList()
                val cost = System.currentTimeMillis() - now
                return AshesKeyHashValue(key, type, ttl, cost, result.asObservable())
            }
            "set" -> {
                val now = System.currentTimeMillis()
                val cost = System.currentTimeMillis() - now
                val set = client().smembers(key)
                return AshesKeySetValue(key, type, ttl, cost, set.toList().asObservable())
            }
            "zset" -> {
                val now = System.currentTimeMillis()
                val cost = System.currentTimeMillis() - now
                val sortedSet = client().zrangeWithScores(key, 0, -1)
                val values = sortedSet.map { Pair(it.score, it.element) }
                return AshesKeySortedSetValue(key, type, ttl, cost, values.asObservable())
            }
            else -> {
                throw RuntimeException("unknown redis value type {}.".format(type))
            }
        }
    }

    fun set(key: String, value: String): AshesKeyValue {
        client().set(key, value)
        return getKeyAndValue(key)
    }

    fun set(ashesKey: AshesNewKey) {
        client().set(ashesKey.key.value, ashesKey.stringValue.value)
        client().expire(ashesKey.key.value, ashesKey.ttl.value)
    }

    fun rpush(key: String, value: List<String>) {
        client().rpush(key, *value.toTypedArray())
    }

    fun sadd(key: String, value: Set<String>) {
        client().sadd(key, *value.toTypedArray())
    }

    fun zadd(key: String, value: Map<String, Double>) {
        client().zadd(key, value)
    }

    fun hmset(key: String, value: Map<String, String>) {
        client().hmset(key, value)
    }

    fun flushDB() {
        client().flushDB()
    }

    fun info(): String {
        return client().info()
    }
}

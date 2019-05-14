package org.tzxyz.ashes.controllers

import org.tzxyz.ashes.global.Current
import org.tzxyz.ashes.models.*
import org.tzxyz.ashes.redis.AshesRedisClientFactory

class AshesKeyController: AshesBaseController() {

    private fun client() = AshesRedisClientFactory.get(Current.getConnection())

    fun getKeyAndValue(key: String): AshesKeyAndValue {
        val ttl = client().ttl(key)
        val type =  client().type(key)
        when(type) {
            "string" -> {
                val now = System.currentTimeMillis()
                val result = client().get(key)
                val cost = System.currentTimeMillis() - now
                return AshesKeyAndStringValue(key, type, ttl, cost, result)
            }
            "list" -> {
                val now = System.currentTimeMillis()
                val length = client().llen(key)
                val result = client().lrange(key, 0, length)
                val cost = System.currentTimeMillis() - now
                return AshesKeyAndListValue(key, type, ttl, cost, result)
            }
            "hash" -> {
                val now = System.currentTimeMillis()
                val result = client().hgetAll(key).toList()
                val cost = System.currentTimeMillis() - now
                return AshesKeyAndHashValue(key, type, ttl, cost, result)
            }
            "set" -> {
                val now = System.currentTimeMillis()
                val cost = System.currentTimeMillis() - now
                val set = client().smembers(key)
                return AshesKeyAndSetValue(key, type, ttl, cost, (0.. set.size).toList().zip(set))
            }
            "zset" -> {
                val now = System.currentTimeMillis()
                val cost = System.currentTimeMillis() - now
                val sortedSet = client().zrangeWithScores(key, 0, -1)
                val values = sortedSet.map { Pair(it.score, it.element) }
                return AshesKeyAndSortedSetValue(key, type, ttl, cost, values)
            }
            else -> {
                throw RuntimeException("unknown redis value type {}.".format(type))
            }
        }
    }
}